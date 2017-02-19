package com.nutrons.framework.util;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class FlowOperators {

  /**
   * Generate a Flowable from a periodic call to a Supplier. Safe Drops Backpressure
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T>     the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier, long ignored, TimeUnit unit) {
    return toFlowBackpressure(supplier, ignored, unit).onBackpressureDrop();
  }

  /**
   * Generate a Flowable from a periodic call to a Supplier. Does NOT drop Backpressure, Beware of overflow!
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T>     the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlowBackpressure(Supplier<T> supplier, long ignored, TimeUnit unit) {
    return Flowable.interval(ignored, unit).subscribeOn(Schedulers.io())
        .map(x -> supplier.get()).observeOn(Schedulers.computation());
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

  /**
   * Creates a function that will return the input value, unless that value is within the range
   * specified by minimum and maximum. If so, the value will be changed to remap.
   */
  public static Function<Double, Double> deadbandMap(double minimum, double maximum, double remap) {
    return bandMap(minimum, maximum, x -> remap);
  }

  /**
   * Creates a function that will return the input value, unless that value is within the range
   * specified by minimum and maximum. If so, the value will be passed through the remap function.
   */
  public static Function<Double, Double> bandMap(double minimum, double maximum, Function<Double, Double> remap) {
    return x -> x < maximum && x > minimum ? remap.apply(x) : x;
  }

  public static <T> T getLastValue(Flowable<T> input) {
    return input.blockingLatest().iterator().next();
  }

  public static FlowableTransformer<Double, Double> pidLoop(double proportional,
                                                            int integralBuffer,
                                                            double integral,
                                                            double derivative) {
    return error -> {
      Flowable<Double> errorP = error.map(x -> x * proportional);
      Flowable<Double> errorI = error.buffer(integralBuffer, 1)
          .map(list -> list.stream().reduce(0.0, (x, acc) -> x + acc))
          .map(x -> x * integral);
      Flowable<Double> errorD = error.buffer(2, 1)
          .map(last -> last.stream().reduce(0.0, (x, y) -> x - y))
          .map(x -> x * derivative);
      Flowable<Double> output = Flowable.combineLatest(errorP, errorI, errorD,
          (p, i, d) -> p + i + d);
      return output;
    };
  }

  public static Function<Double, Double> limitWithin(double minimum, double maximum) {
    return x -> max(min(x, maximum), minimum); 
  }
    
  public static <T> T printId(T t) {
    System.out.println(t);
    return t;
  }
}
