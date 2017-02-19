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
}
