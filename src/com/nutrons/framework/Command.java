package com.nutrons.framework;

import com.nutrons.framework.util.FlowOperators;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.processors.PublishProcessor;
import java.util.function.Supplier;

public class Command {
  private final PublishProcessor<Action> actions;
  private final Flowable<Action> out;

  private Command(Action action) {
    this(Flowable.just(action));
  }

  private Command(Flowable<Action> actions) {
    this.actions = PublishProcessor.create();
    this.out = this.actions.mergeWith(actions);
  }

  public Flowable<Action> stream() {
    return this.out;
  }

  public Command then(Command next) {
    return new Command(this.out.concatWith(next.out));
  }

  public static Command create(Action action) {
    return new Command(action);
  }

  public static Command create(Flowable<Action> actions) {
    return new Command(actions);
  }

  public static Command createWithCondition(Action action, Supplier<Boolean> endCondition) {
    Command command = Command.create(action);
    FlowOperators.toFlow(() -> endCondition).filter(Supplier::get).take(1)
        .subscribe(x -> command.actions.onComplete());
    return command;
  }

  public static Command parallel(Command... commands) {
    return new Command(Observable.fromArray(commands).map(x -> (Flowable) x.out)
        .reduce((a, x) -> a.mergeWith(x)).blockingGet());
  }
}
