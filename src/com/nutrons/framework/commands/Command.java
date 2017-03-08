package com.nutrons.framework.commands;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.reactivestreams.Publisher;

public class Command implements CommandWorkUnit {

  // emptyPulse sends items on an interval, and is used in the 'until' and 'when' methods
  // to determine how often to test the predicates.
  private static final ConnectableFlowable<CommandWorkUnit> emptyPulse =
      toFlow(() -> (CommandWorkUnit) x -> Flowable.just(() -> {
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
    return Command.just(x -> {
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
    return Command.just(x -> {
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

  /**
   * Creates a command that runs sequentially to another.
   *
   * @param commandStream A flowable of commands
   * @retuns Second command after first is executed.
   */
  public static Command fromSwitch(Publisher<? extends CommandWorkUnit> commandStream) {
    return new Command(x -> Flowable.defer(() ->
        Flowable.switchOnNext(Flowable.fromPublisher(commandStream).map(y -> y.execute(x))
            .subscribeOn(Schedulers.io()))).scan((a, b) -> {
      a.run();
      return b;
    }));
  }

  public Command addFinalTerminator(Terminator terminator) {
    return Command.just(x -> this.source.execute(x).flatMap(y -> Flowable.<Terminator>just(y, terminator)).subscribeOn(Schedulers.io()));
  }

  /**
   * Copies this command into one which will not execute until 'starter' emits an item.
   */
  public Command startable(Publisher<?> starter) {
    return new Command(new SerialCommand(
        Command.just(x -> Flowable.defer(() -> Flowable.<Terminator>never().takeUntil(starter))),
        this));
  }

  /**
   * Copies this command into one which, when executed,
   * will delay the execution of all actions until startCondition returns true.
   */
  public Command when(Supplier<Boolean> startCondition) {
    Flowable ignition = Flowable
        .defer(() -> emptyPulse.map(x -> startCondition.get()).filter(x -> x).onBackpressureDrop());
    return this.startable(ignition);
  }

  /**
   * Copies this command into one which will end and terminate
   * only when the terminator emits an item.
   */
  public Command terminable(Publisher<?> terminator) {
    return this.endsWhen(terminator, false);
  }

  /**
   * Copies this command into one which will end when terminator emits an item.
   *
   * @param terminatesAtEnd if true, the command will terminate only when the end is reached,
   *                        if false, the command may terminate before the end is reached.
   */
  public Command endsWhen(Publisher<?> terminator, boolean terminatesAtEnd) {
    return Command.just(x -> {
      Flowable<Terminator> sourceTerminator = this.execute(terminatesAtEnd);
      Terminator multi = FlattenedTerminator.from(sourceTerminator);
      return Flowable.defer(() -> Flowable.<Terminator>never().takeUntil(terminator)
          .mergeWith(Flowable.just(multi::run)));
    });
  }

  /**
   * Copies this command into one which, when executed,
   * will only complete once endCondition returns true.
   */
  public Command until(Supplier<Boolean> endCondition) {
    ConnectableFlowable<?> terminator = emptyPulse.map(x -> endCondition.get()).filter(x -> x).onBackpressureDrop().publish();
    terminator.connect();
    return this.terminable(terminator);
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

  /**
   * Copies this command into one which will delay termination until a period of time has passed.
   */
  public Command delayFinish(long delay, TimeUnit unit) {
    return this.terminable(Flowable.timer(delay, unit));
  }

  public Command killAfter(long delay, TimeUnit unit) {
    return Command.just(x -> {
      Flowable<Terminator> terms = this.terminable(Flowable.timer(delay, unit)).execute(x);
      return terms;
    });
  }

  @Override
  public Flowable<Terminator> execute(boolean selfTerminating) {
    Flowable<Terminator> terms = source.execute(selfTerminating).subscribeOn(Schedulers.io());
    if (selfTerminating) {
      terms.toList().subscribe(x -> Observable.fromIterable(x).blockingSubscribe(Terminator::run));
    }
    return terms;
  }

  public static PartialCommand beginWith(Runnable action) {
    return new PartialCommand(action);
  }

  public static class PartialCommand {
    private final Runnable action;

    PartialCommand(Runnable action) {
      this.action = action;
    }

    public Command endWith(Runnable cleanup) {
      return Command.just(x -> {
        action.run();
        return Flowable.just(new TerminatorWrapper(cleanup));
      });
    }
  }
}
