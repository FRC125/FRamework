package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;

import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class TestConnectables {

  @Test
  public void testConnectableKeeps() throws InterruptedException {
    ConnectableFlowable<Long> flow = Flowable.interval(1, TimeUnit.MILLISECONDS).publish();
    Thread.sleep(1000);
    flow.take(1).subscribeOn(Schedulers.io()).subscribe(x -> assertTrue(x == 0));
    flow.connect();
    Thread.sleep(1000);
  }

  @Test
  public void testConnectableDiscards() throws InterruptedException {
    ConnectableFlowable<Long> flow = Flowable.interval(1, TimeUnit.MILLISECONDS).publish();
    flow.connect();
    Thread.sleep(1000);
    flow.take(1).subscribeOn(Schedulers.io()).subscribe(x -> assertTrue(x != 0));
    Thread.sleep(1000);
  }

  @Test
  public void testConnectableWaitsToRetrieve() throws InterruptedException {
    Runnable avoid = () -> assertTrue(false);
    Runnable wanted = () -> {};
    ConnectableFlowable<Runnable> source = Flowable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.io()).map(x -> avoid)
        .take(5).concatWith(Flowable.timer(3, TimeUnit.SECONDS).map(x -> wanted)).publish();
    source.connect();
    ConnectableFlowable<Runnable> listener = source.take(1).publish();
    listener.subscribe(Runnable::run);
    Thread.sleep(6000);
    listener.connect();
    Thread.sleep(1000);
  }
}
