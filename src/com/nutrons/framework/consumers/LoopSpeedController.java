package com.nutrons.framework.consumers;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import org.reactivestreams.Subscription;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController implements Consumer<ControllerEvent> {
  private final List<Subscription> subscriptions = new ArrayList<>();
  private final int port;

  public LoopSpeedController(int port) {
    this.port = port;
  }

  public void unsub() {
    Observable.fromIterable(this.subscriptions).blockingSubscribe(x -> x.cancel());
  }

  public int port() {
    return port;
  }
}
