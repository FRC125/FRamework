package com.nutrons.FRamework.consumers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 * Extends observable so it can contain state, which may be necessary in the future.
 */
public abstract class LoopSpeedController implements Subscriber<LoopControllerEvent> {
  private final List<Subscription> subscriptions = new ArrayList<>();

  @Override
  public void onSubscribe(Subscription s) {
    this.subscriptions.add(s);
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {

  }

  public void unsub() {
    Observable.fromIterable(this.subscriptions).blockingSubscribe(x -> x.cancel());
  }

  abstract void set(double value);

  abstract void setLoopProperties(double p, double i, double d, double f);
}
