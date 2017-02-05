package com.nutrons.framework.util;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import java.util.function.Supplier;

public class Command {
  private final Flowable<Action> output;
  private static final Flowable<Action> emptyPulse = toFlow(() -> (Action) () -> {
  }).observeOn(Schedulers.io());

  private Command(Flowable<Action> actions) {
    this.output = actions.observeOn(Schedulers.io());
  }

  private Command(Action action) {
    this(Flowable.just(action));
  }

  /**
   * Begin an execution of the command.
   */
  public Disposable execute() {
    return this.output.flatMap(x -> Maybe.fromAction(x)
        .subscribeOn(Schedulers.io()).toFlowable()).subscribe();
  }

  /**
   * Create a command that executes the given action.
   */
  public static Command create(Action action) {
    return new Command(action);
  }

  /**
   * Create a command that executes the given actions contained within the Flowable.
   */
  public static Command create(Flowable<Action> actions) {
    return new Command(actions);
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
    Observable<Flowable<Action>> cmds = Observable.fromArray(commands).map(x -> x.output);
    // Below, each Flowable is replaced, so that it is only called after the previous one completes.
    Observable<Flowable<Action>> concatenated = cmds.scan((p, t) -> p.ignoreElements()
        .<Action>toFlowable().concatWith(t));
    // p is the previous Flowable in the sequence, and t is the current Flowable.
    return new Command(Flowable.concat(concatenated.toList().blockingGet()));
  }

  /**
   * Create a command that executes the provided commands in parallel.
   */
  public static Command parallel(Command... commands) {
    return new Command(Flowable.fromArray(commands).flatMap(x -> x.output));
  }

  /**
   * Copy this command, to form one which will delay
   * execution until the 'start trigger' Action is called.
   */
  public Pair<Command, Action> startTrigger() {
    PublishProcessor<Action> pp = PublishProcessor.create();
    return new Pair<>(new Command(pp.concatWith(this.output)), pp::onComplete);
  }

  /**
   * Copy this command, to form one which has a 'start condition.'
   * The start condition supplies true at any point in time
   * after the command is considered to have commenced.
   */
  public Command startsWhen(Supplier<Boolean> startCondition) {
    Pair<Command, Action> pair = this.startTrigger();
    emptyPulse.filter(x -> startCondition.get()).firstElement()
        .subscribeOn(Schedulers.io()).subscribe(x -> pair.right().run());
    return pair.left();
  }

  /**
   * Copy this command, to form one which will complete
   * only when the 'end trigger' Action is called.
   */
  public Pair<Command, Action> endTrigger() {
    PublishProcessor<Action> pp = PublishProcessor.create();
    return new Pair<>(new Command(
        this.output.mergeWith(Flowable.never()).takeUntil(pp)), pp::onComplete);
  }

  /**
   * Copy this command, to form one which has an 'end condition.'
   * The end condition supplies true at any point in time
   * after the command is considered to have concluded.
   */
  public Command endsWhen(Supplier<Boolean> endCondition) {
    Pair<Command, Action> pair = this.endTrigger();
    emptyPulse.filter(x -> endCondition.get()).firstElement()
        .subscribeOn(Schedulers.io()).subscribe(x -> pair.right().run());
    return pair.left();
  }

  /**
   * Provides a copy of this command, which starts and ends
   * when the respective Suppliers provide the value of true.
   */
  public Command when(Supplier<Boolean> start, Supplier<Boolean> end) {
    return this.startsWhen(start).endsWhen(end);
  }
}
