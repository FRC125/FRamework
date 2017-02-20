package com.nutrons.framework.test;

import static com.nutrons.framework.util.FlowOperators.pidLoop;
import static com.nutrons.framework.util.FlowOperators.toFlow;

import io.reactivex.Flowable;
import org.junit.Test;

public class TestPID {

  @Test
  public void testPIDConcurrency() {
    Flowable<Double> input = toFlow(() -> 1.0);
    Flowable<Double> output = input.compose(pidLoop(1, 1, 0, 0));
    output.subscribe(x -> {
    }); // tests that this is done on an io scheduler
  }
}
