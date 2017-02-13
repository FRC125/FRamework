package com.nutrons.framework.util;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class FlowOperators {

  /**
   * Generate a Flowable from a periodic call to a Supplier. Safe Drops Backpressure
   *
   * @param delay the number of time units to wait before calling the supplier again
   * @param <T>   the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier, long delay, TimeUnit unit) {
    return toFlowBackpressure(supplier, delay, unit).onBackpressureDrop();
  }

  /**
   * Generate a Flowable from a periodic call to a Supplier. Does NOT drop Backpressure, Beware of overflow!
   *
   * @param delay the number of time units to wait before calling the supplier again
   * @param <T>   the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlowBackpressure(Supplier<T> supplier, long delay, TimeUnit unit) {
    return Flowable.interval(delay, unit, Schedulers.trampoline())
        .observeOn(Schedulers.io()).map((x) -> supplier.get()).observeOn(Schedulers.computation());
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
   * Produces a function where, if the input is within the minimum and maximum range, will emit
   * the value specified by remap. If the value is not within this range,
   * the function returns the input.
   */
  public static Function<Double, Double> deadband(double minimum, double maximum, double remap) {
    return bandMap(minimum, maximum, x -> remap);
  }

  public static Function<Double, Double> bandMap(double minimum, double maximum, Function<Double, Double> remap) {
    return x -> x < maximum && x > minimum ? remap.apply(x) : x;
  }

  public static <T> T getLastValue(Flowable<T> input) {
    return input.blockingLatest().iterator().next();
  }
}
