package com.nutrons.framework.commands;

import com.nutrons.framework.util.FlowOperators;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.nutrons.framework.util.FlowOperators.printId;
import static com.nutrons.framework.util.FlowOperators.toFlow;

public class Command implements CommandWorkUnit {
  // emptyPulse sends items on an interval, and is used in the 'until' and 'when' methods
  // to determine how often to test the predicates.
  private static final ConnectableFlowable<CommandWorkUnit> emptyPulse =
      toFlow(() -> (CommandWorkUnit) () -> Flowable.just(() -> {
      })).subscribeOn(Schedulers.io()).onBackpressureDrop().publish();
  private final CommandWorkUnit source;

  private Command(CommandWorkUnit command) {
    emptyPulse.connect();
    this.source = command;
  }

  /**
   * Create a command that executes the given action.
   */
  public static Command fromAction(Runnable action) {
    return new Command(() -> {
      action.run();
      return Flowable.just(() -> {
      });
    });
  }

  /**
   * Create a command from a disposable supplying function,
   * such as a function which subscribes a stream to a consumer.
   */
  public static Command fromSubscription(Supplier<Disposable> resource) {
    return Command.just(() -> {
      Disposable disposable = resource.get();
      return Flowable.just(new TerminatorWrapper(disposable::dispose));
    });
  }

  /**
   * Create a command that executes the given work unit, and has custom termination functionality.
   */
  public static Command just(CommandWorkUnit start) {
    return new Command(start);
  }

  /**
   * Create a command that executes the provided commands sequentially.
   *
   * @param commands these commands will be executed from 'first to last' argument.
   */
  public static Command serial(Command... commands) {
    return new Command(new SerialCommand(commands));
  }

  /**
   * Create a command that executes the provided commands in parallel.
   */
  public static Command parallel(Command... commands) {
    return new Command(new ParallelCommand(commands));
  }

  public static Command fromSwitch(Publisher<? extends CommandWorkUnit> commandStream) {
    Flowable<Terminator> commands = Flowable.defer(() ->
        Flowable.switchOnNext(Flowable.fromPublisher(commandStream).map(CommandWorkUnit::execute)
            .subscribeOn(Schedulers.io())));
    return new Command(() -> commands.scan((a, b) -> {
      a.run();
      return b;
    }));
  }

  public Command addFinalTerminator(Terminator terminator) {
    return Command.just(() -> this.source.execute().flatMap(x -> Flowable.just(x, terminator)));
  }

  /**
   * Copies this command into one which will not execute until 'starter' emits an item.
   */
  public Command startable(Publisher<?> starter) {
    return new Command(new SerialCommand(
        () -> Flowable.defer(() -> Flowable.<Terminator>never().takeUntil(starter)), this));
  }

  /**
   * Copies this command into one which, when executed,
   * will delay the execution of all actions until startCondition returns true.
   */
  public Command when(Supplier<Boolean> startCondition) {
    Flowable ignition = Flowable.defer(() -> emptyPulse.map(x -> startCondition.get()).filter(x -> x).onBackpressureDrop());
    return this.startable(ignition);
  }

  /**
   * Copies this command into one which will end when terminator emits an item.
   */
  public Command terminable(Publisher<?> terminator) {
    return new Command(() -> {
      ConnectableFlowable<?> flowterminator = Flowable.fromPublisher(terminator).publish();
      flowterminator.connect();
      Flowable<Terminator> sourceTerminator = this.execute();
      Terminator multi = FlattenedTerminator.from(sourceTerminator);
      return Flowable.defer(() -> Flowable.<Terminator>never().takeUntil(flowterminator)
          .mergeWith(Flowable.just(multi::run)));
    });
  }

  /**
   * Copies this command into one which, when executed,
   * will only complete once endCondition returns true.
   */
  public Command until(Supplier<Boolean> endCondition) {
    Flowable terminator = emptyPulse.map(x -> endCondition.get()).filter(x -> x).onBackpressureDrop().publish();
    return new Command(this::execute).terminable(terminator);
  }

  /**
   * Create a command that executes this command, and then, sequentially, the 'next' command.
   */
  public Command then(Command next) {
    return Command.serial(this, next);
  }

  /**
   * Copies this command into one which will delay execution for a period of time.
   */
  public Command delayStart(long delay, TimeUnit unit) {
    return this.startable(Flowable.timer(delay, unit));
  }

  public Command killAfter(long delay, TimeUnit unit) {
    return Command.just(() -> {
      Flowable<Terminator> terms = this.terminable(Flowable.timer(delay, unit)).execute();
      return terms.doOnComplete(() -> {
        FlattenedTerminator.from(terms).toSingle()
            .subscribe(Terminator::run);
      });
    });
  }

  @Override
  public Flowable<Terminator> execute() {
    Flowable<Terminator> terms = source.execute();
    return terms;
  }

  public Flowable<Terminator> startExecution() {
    Flowable<Terminator> terms = this.execute().subscribeOn(Schedulers.io());
    terms.toList().subscribe(x -> Observable.fromIterable(x).blockingSubscribe(Terminator::run));
    return terms;
  }
}
