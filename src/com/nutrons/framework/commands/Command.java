package com.nutrons.framework.commands;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.nutrons.framework.util.FlowOperators.toFlow;

public class Command {
  private final Flowable<CommandWorkUnit> output;
  private static final Flowable<CommandWorkUnit> emptyPulse = toFlow(() -> (CommandWorkUnit) () -> (Terminator) () -> {
  }).subscribeOn(Schedulers.io());

  private Command(CommandWorkUnit action) {
    this(Flowable.just(action));
  }

  private Command(Flowable<CommandWorkUnit> actions) {
    this.output = actions;
  }

  /**
   * Begin an execution of the command.
   */
  public Disposable execute() {
    return this.output.flatMap(x ->
        Flowable.fromCallable(x::get).subscribeOn(Schedulers.io()
        )).subscribeOn(Schedulers.io()).subscribe(Terminator::run);
  }

  /**
   * Create a command that executes the given action.
   */
  public static Command create(Runnable action) {
    return new Command(() -> {
      action.run();
      return () -> {
      };
    });
  }

  public static Command from(Supplier<Disposable> resource) {
    return Command.create(() -> {
      Disposable disposable = resource.get();
      return disposable::dispose;
    });
  }

  /**
   * Create a command that executes the given supplier, and has custom termination functionality.
   */
  public static Command create(CommandWorkUnit start) {
    return new Command(start);
  }

  /**
   * Copies this command into one which will not execute until 'starter' emits an item.
   */
  public Command startable(Publisher<?> starter) {
    return new Command(Flowable.defer(() ->
        Flowable.<CommandWorkUnit>never().takeUntil(starter).concatWith(this.output)));
  }

  /**
   * Copies this command into one which, when executed,
   * will delay the execution of all actions until startCondition returns true.
   */
  public Command when(Supplier<Boolean> startCondition) {
    ConnectableFlowable ignition = emptyPulse.map(x -> startCondition.get()).filter(x -> x).publish();
    ignition.connect();
    return this.startable(ignition);
  }

  /**
   * Copies this command into one which will execute until 'terminator' emits an item.
   */
  public Command terminable(Publisher<?> terminator) {
    return new Command(Flowable.defer(() ->
        this.output.takeUntil(terminator)));
  }

  /**
   * Copies this command into one which, when executed,
   * will only complete once endCondition returns true.
   */
  public Command until(Supplier<Boolean> endCondition) {
    ConnectableFlowable terminator = emptyPulse.map(x -> endCondition.get()).filter(x -> x).publish();
    terminator.connect();
    return new Command(this.output.mergeWith(Flowable.never())).terminable(terminator);
  }

  /**
   * Create a command that executes this command, and then, sequentially, the 'next' command.
   */
  public Command then(Command next) {
    return Command.serial(this, next);
  }

  /**
   * Create a command that executes the provided commands sequentially.
   *
   * @param commands these commands will be executed from 'first to last' argument.
   */
  public static Command serial(Command... commands) {
    return new Command(Flowable.defer(() ->
        Flowable.fromArray(commands).concatMap(x -> x.output)));
  }

  /**
   * Create a command that executes the provided commands in parallel.
   */
  public static Command parallel(Command... commands) {
    return new Command(Flowable.defer(() ->
        Flowable.merge(Flowable.fromArray(commands).map(c -> c.output))));
  }

  /**
   * Copies this command into one which will delay execution for a period of time.
   */
  public Command delayStart(long delay, TimeUnit unit) {
    return this.startable(Flowable.timer(delay, unit));
  }

  /**
   * Copies this command into one which will delay its completion until a certain time has passed.
   */
  public Command delayFinish(long delay, TimeUnit unit) {
    return parallel(this, new Command(Flowable.never()).terminable(Flowable.timer(delay, unit)));
  }

  public static Command fromSwitch(Publisher<Command> commandStream) {
    Flowable<CommandWorkUnit> actions = Flowable.switchOnNext(Flowable.fromPublisher(commandStream)
        .map(x -> x.output));
    return new Command(actions);
  }

  public Command killAfter(long delay, TimeUnit unit) {
    return new Command(this.terminable(Flowable.timer(delay, unit)).output);
  }
}
