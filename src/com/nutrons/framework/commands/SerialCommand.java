package com.nutrons.framework.commands;

import io.reactivex.Flowable;

public class SerialCommand implements CommandWorkUnit {

  private final Flowable<Command> commands;

  SerialCommand(CommandWorkUnit... commands) {
    this.commands = Flowable.fromArray(commands).concatMap(x -> {
      if (x instanceof SerialCommand) {
        return ((SerialCommand) x).commands;
      }
      return Flowable.just(Command.just(x));
    });
  }

  @Override
  public Flowable<Terminator> execute(boolean selfTerminating) {
    Flowable<? extends Terminator> terminators = this.commands
        .concatMap(x -> x.execute(selfTerminating)).replay().autoConnect();
    return Flowable.<Terminator>just(FlattenedTerminator.from(terminators))
        .mergeWith(terminators.ignoreElements().toFlowable());
  }
}
