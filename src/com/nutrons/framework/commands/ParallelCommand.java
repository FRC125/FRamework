package com.nutrons.framework.commands;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class ParallelCommand implements CommandWorkUnit {

  private final Flowable<Command> commands;

  ParallelCommand(CommandWorkUnit... commands) {
    this.commands = Flowable.fromArray(commands).map(Command::just);
  }

  @Override
  public Flowable<Terminator> execute(boolean selfTerminating) {
    Flowable<Terminator> terminators = this.commands
        .flatMap(x -> x.execute(selfTerminating).subscribeOn(Schedulers.io()))
        .subscribeOn(Schedulers.io()).publish().autoConnect();
    return Flowable.<Terminator>just(FlattenedTerminator.from(terminators))
        .mergeWith(terminators.ignoreElements().toFlowable());
  }
}
