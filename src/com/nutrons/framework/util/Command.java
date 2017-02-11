package com.nutrons.framework.util;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.reactivestreams.Publisher;

public class Command {
  private final Flowable<Action> output;
  private static final Flowable<Action> emptyPulse = toFlow(() -> (Action) () -> {
  }).subscribeOn(Schedulers.io());

  /**
   * @param actions the actions this command will execute, sequentially.
   */
  private Command(Flowable<Action> actions) {
    this.output = actions;
  }

  private Command(Action action) {
    this(Flowable.just(action));
  }

  /**
   * Begin an execution of the command.
   */
  public Disposable execute() {
    return this.output.concatMap(x ->
        Maybe.fromAction(x).toFlowable().subscribeOn(Schedulers.io()
        )).subscribeOn(Schedulers.io()).subscribe();
  }

  /**
   * Create a command that executes the given action.
   */
  public static Command create(Action action) {
    return new Command(action);
  }

  /**
   * Create a command that executes the given actions contained within the Flowable in series.
   */
  public static Command create(Flowable<Action> series) {
    return new Command(series);
  }

  /**
   * Copies this command into one which will not execute until 'starter' emits an item.
   */
  public Command startable(Publisher<?> starter) {
    return new Command(Flowable.defer(() ->
        Flowable.<Action>never().takeUntil(starter).concatWith(this.output)));
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
    return new Command(Flowable.concat(Flowable.fromArray(commands).map(x -> x.output)));
  }

  /**
   * Create a command that executes the provided commands in parallel.
   */
  public static Command parallel(Command... commands) {
    return new Command(Flowable.merge(
        Flowable.fromArray(commands).map(c ->
            c.output.concatMap(a ->
                Maybe.<Action>fromAction(a).toFlowable().subscribeOn(Schedulers.io())
            )
        )
    ));
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

  public Command fromSwitch(Publisher<Command> commandStream) {
    Flowable<Action> actions = Flowable.switchOnNext(Flowable.fromPublisher(commandStream)
        .map(x -> x.output));
    return new Command(actions);
  }

  public Command killAfter(long delay, TimeUnit unit) {
    return new Command(this.terminable(Flowable.timer(delay, unit)).output);
  }
}
