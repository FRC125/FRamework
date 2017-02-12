package com.nutrons.framework.test;

import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by krzw9 on 2/12/2017.
 */
public class TestPidFlow {

  @Test
  public void testIntegralScan(){
    double alpha = 0.9;
    List<Double> vals =new ArrayList<>();
    Flowable.just(1.,2.,3.,4.,5.).scan((acc,x) -> acc*(1.0-alpha) + x*alpha)
        .subscribe(x -> vals.add(x));

    assertEquals(4.889, vals.get(4), 0.01);
  }

}
