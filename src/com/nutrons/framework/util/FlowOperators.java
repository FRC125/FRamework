package com.nutrons.framework.util;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.lang.Math.abs;

public class FlowOperators {
  /**
   * Generate a Flowable from a periodic call to a Supplier.
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T>     the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier, long ignored, TimeUnit unit) {
    return Flowable.interval(ignored, unit, Schedulers.io())
        .subscribeOn(Schedulers.io()).map((x) -> supplier.get());
  }

  /**
   * Generate a Flowable from a periodical call to a Supplier.
   *
   * @param <T> the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier) {
    return toFlow(supplier, 100, TimeUnit.MILLISECONDS);
  }

  /**
   * Modifies motor input to prevent motors running at very small values
   * @param input the raw motor input values
   * @return the motor input values with values smaller than 0.2 removed
   */
  public static Flowable<Double> deadband(Flowable<Double> input) {
    return input.map((x) -> abs(x) < 0.2 ? 0.0 : x);
  }
}
