package com.nutrons.framework.commands;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class SerialCommand implements CommandWorkUnit {

  private final Flowable<CommandWorkUnit> commands;

  SerialCommand(CommandWorkUnit... commands) {
    this.commands = Flowable.fromArray(commands);

  }

  @Override
  public Flowable<Terminator> execute() {
    Flowable<Terminator> terminators = this.commands
        .concatMap(x -> x.execute().subscribeOn(Schedulers.io()))
        .subscribeOn(Schedulers.io()).publish().autoConnect();

    return Flowable.<Terminator>just(FlattenedTerminator.from(terminators))
        .mergeWith(terminators.ignoreElements().toFlowable());
  }
}
