package com.nutrons.framework.test;

import static com.nutrons.framework.util.FlowOperators.pdLoop;
import static com.nutrons.framework.util.FlowOperators.toFlow;

import io.reactivex.Flowable;
import org.junit.Test;

public class TestPD {

    @Test
    public void testPDConcurrency (){
      Flowable<Double> input = toFlow(() -> 1.0);
      Flowable<Double> output = input.compose(pdLoop(1.0, 1.0));
      output.subscribe(x -> {
      }); //tests
    }
}
