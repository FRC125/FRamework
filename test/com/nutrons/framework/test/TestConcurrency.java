package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class TestConcurrency {

  @Test
  public void mergingInParallel() {
    Flowable<Long> even = Flowable.interval(1000, TimeUnit.MILLISECONDS).map(x -> 2 * x)
        .onBackpressureBuffer();

    // odd and even are concat-mapped, such that they become effectively callbacks.
    Flowable<Long> odd = even.concatMap(x -> callBackWith(x + 1, 2000));

    even = even.concatMap(x -> callBackWith(x, 5000));

    // Once they are merged, even numbers and odd numbers are expected to run in series relative to their own events, but parallel relative to eachother.
    Flowable<Long> combo = Flowable.merge(even, odd);
    // combo.subscribe(System.out::println);
    long[] lastVals = new long[2];
    lastVals[0] = -1;
    lastVals[1] = -1;
    combo.subscribe(x -> {
      if (x % 2 == 0) {
        assertTrue(lastVals[0] < x);
        lastVals[0] = x;
      } else {
        assertTrue(lastVals[1] < x);
        lastVals[1] = x;
      }
    });
    assertTrue(combo.take(3).filter(x -> x == 0).toList().blockingGet().size() > 0);
    assertFalse(combo.take(3).filter(x -> x == 4).toList().blockingGet().size() > 0);
  }


  private Flowable<Long> callBackWith(long x, long time) {
    return Flowable.timer(time, TimeUnit.MILLISECONDS, Schedulers.io()).map(y -> x);
  }
}
