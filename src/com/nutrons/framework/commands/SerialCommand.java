package com.nutrons.framework.commands;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialCommand implements CommandWorkUnit {
  private final Flowable<CommandWorkUnit> commands;

  SerialCommand(CommandWorkUnit... commands) {
    this.commands = Flowable.fromArray(commands);

  }

  @Override
  public Flowable<Terminator> execute() {
    final AtomicBoolean lock = new AtomicBoolean(false);
    final List<Terminator> terminators = new ArrayList<>();
    Flowable<Terminator> terminatorFlow = this.commands.concatMap(x -> x.execute().subscribeOn(Schedulers.io()))
        .subscribeOn(Schedulers.io()).publish().autoConnect();
    terminatorFlow.subscribe(x -> {
      if (!lock.get()) {
        synchronized (lock) {
          if (!lock.get()) {
            terminators.add(x);
            return;
          }
        }
      }
      x.run();
    });
    return Flowable.<Terminator>just(
        new TerminatorWrapper(new SerialTerminator(lock, terminators)))
        .mergeWith(terminatorFlow.ignoreElements().toFlowable());
  }

  private class SerialTerminator implements Runnable {
    private final AtomicBoolean lock;
    private final List<Terminator> terminators;

    private SerialTerminator(AtomicBoolean lock, List<Terminator> terminators) {
      this.lock = lock;
      this.terminators = terminators;
    }

    @Override
    public void run() {
      synchronized (lock) {
        lock.set(true);
        for (Terminator terminator : terminators) {
          terminator.run();
        }
      }
    }
  }
}