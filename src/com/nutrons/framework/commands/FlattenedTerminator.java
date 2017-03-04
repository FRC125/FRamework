package com.nutrons.framework.commands;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

class FlattenedTerminator implements Terminator {

  private final AtomicBoolean lock;
  private final ArrayList<Terminator> terminators;
  private final Flowable<? extends Terminator> terminatorStream;

  private FlattenedTerminator(Flowable<? extends Terminator> terminators) {
    this.lock = new AtomicBoolean(false);
    this.terminators = new ArrayList<>();
    this.terminatorStream = terminators;
    terminators.subscribe(x -> {
      if (!lock.get()) {
        synchronized (lock) {
          if (!lock.get()) {
            this.terminators.add(x);
            return;
          }
        }
      }
      x.run();
    });
  }

  static FlattenedTerminator from(Flowable<? extends Terminator> terminators) {
    return new FlattenedTerminator(terminators);
  }

  public Flowable<? extends Terminator> toSingle() {
    return Flowable.just(this).mergeWith(terminatorStream.ignoreElements().toFlowable())
        .subscribeOn(Schedulers.io());
  }

  @Override
  public void run() {
    if (!lock.get()) {
      synchronized (lock) {
        if (!lock.get()) {
          lock.set(true);
          for (Terminator terminator : terminators) {
            terminator.run();
          }
        }
      }
    }
  }
}
