package com.nutrons.FRamework.consumers;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 * Extends observable so it can contain state, which may be necessary in the future.
 */
public abstract class LoopSpeedController implements Observer<LoopControllerEvent> {
  private final List<Disposable> subscriptions = new ArrayList<>();

  @Override
  public void onSubscribe(Disposable disposable) {
    this.subscriptions.add(disposable);
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {

  }

  public void unsub() {
    Observable.fromIterable(this.subscriptions).blockingSubscribe(x -> x.dispose());
  }

  abstract void set(double value);

  abstract void setLoopProperties(double p, double i, double d, double f);
}
