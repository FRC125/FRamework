package com.nutrons.FRamework.consumers;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Subscription;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController implements Consumer<LoopControllerEvent> {
  private final List<Subscription> subscriptions = new ArrayList<>();

  public void unsub() {
    Observable.fromIterable(this.subscriptions).blockingSubscribe(x -> x.cancel());
  }

  abstract void set(double value);

  abstract void setLoopProperties(double p, double i, double d, double f);
}
