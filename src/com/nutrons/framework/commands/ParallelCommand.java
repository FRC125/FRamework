package com.nutrons.framework.commands;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class ParallelCommand implements CommandWorkUnit {
  private final Flowable<CommandWorkUnit> commands;

  ParallelCommand(CommandWorkUnit... commands) {
    this.commands = Flowable.fromArray(commands);

  }

  @Override
  public Flowable<Terminator> execute() {
    PublishProcessor<Terminator> finisher = PublishProcessor.create();
    final ExecuteLock lock = new ExecuteLock();
    final List<Terminator> terminators = new ArrayList<>();
    Flowable<Terminator> terminatorFlow = this.commands.flatMap(x -> x.execute().subscribeOn(Schedulers.io()))
        .subscribeOn(Schedulers.io());
    terminatorFlow.subscribe(x -> {
      if (!lock.done) {
        synchronized (lock) {
          if (!lock.done) {
            terminators.add(x);
            return;
          }
        }
      }
      x.run();
    });
    return Flowable.<Terminator>just(
        new TerminatorWrapper(new ParallelTerminator(lock, terminators)))
        .mergeWith(terminatorFlow.ignoreElements().toFlowable());
  }

  private class ParallelTerminator implements Runnable {
    private final ExecuteLock lock;
    private final List<Terminator> terminators;

    private ParallelTerminator(ExecuteLock lock, List<Terminator> terminators) {
      this.lock = lock;
      this.terminators = terminators;
    }

    @Override
    public void run() {
      synchronized (lock) {
        lock.done = true;
        for (Terminator terminator : terminators) {
          terminator.run();
        }
      }
    }
  }

  private class ExecuteLock {
    private boolean done = false;
  }
}
