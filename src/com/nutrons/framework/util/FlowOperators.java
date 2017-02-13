package com.nutrons.framework.util;

import static java.lang.Math.abs;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class FlowOperators {

  /**
   * Generate a Flowable from a periodic call to a Supplier. Safe Drops Backpressure
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T> the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier, long ignored, TimeUnit unit) {
    return toFlowBackpressure(supplier, ignored, unit).onBackpressureDrop();
  }

  /**
   * Generate a Flowable from a periodic call to a Supplier. Does NOT drop Backpressure, Beware of overflow!
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T> the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlowBackpressure(Supplier<T> supplier, long ignored, TimeUnit unit) {
    return Flowable.interval(ignored, unit, Schedulers.trampoline())
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
   * Modifies motor input to prevent motors running at very small values
   *
   * @param input the raw motor input values
   * @return the motor input values with values smaller than 0.2 removed
   */
  public static Flowable<Double> deadband(Flowable<Double> input) {
    return input.map((x) -> abs(x) < 0.4 ? 0.0 : x);
  }

  public static Function<Double, Double> deadbandMap(double minimum, double maximum, double remap) {
    return bandMap(minimum, maximum, x -> remap);
  }

  public static Function<Double, Double> bandMap(double minimum, double maximum, Function<Double, Double> remap) {
    return x -> x < maximum && x > minimum ? remap.apply(x) : x;
  }

  public static <T> T getLastValue(Flowable<T> input) {
    return input.blockingLatest().iterator().next();
  }
}
