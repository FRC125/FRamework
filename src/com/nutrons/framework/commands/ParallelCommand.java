package com.nutrons.framework.commands;

import io.reactivex.Flowable;

public class ParallelCommand implements CommandWorkUnit {

  private final Flowable<Command> commands;

  ParallelCommand(CommandWorkUnit... commands) {
    this.commands = Flowable.fromArray(commands).concatMap(x -> {
      if (x instanceof ParallelCommand) {
        return ((ParallelCommand) x).commands;
      }
      return Flowable.just(Command.just(x));
    });
  }

  @Override
  public Flowable<Terminator> execute(boolean selfTerminating) {
    Flowable<? extends Terminator> terminators = this.commands
        .flatMap(x -> x.execute(selfTerminating)).replay().autoConnect();
    return Flowable.<Terminator>just(FlattenedTerminator.from(terminators))
        .mergeWith(terminators.ignoreElements().toFlowable());
  }
}
