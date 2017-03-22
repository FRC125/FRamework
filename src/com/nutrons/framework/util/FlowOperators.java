package com.nutrons.framework.util;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


public class FlowOperators {

  /**
   * Generate a Flowable from a periodic call to a Supplier. Drops on backpressure.
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T> the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier,
      long ignored, TimeUnit unit) {
    return Flowable.interval(ignored, unit).subscribeOn(Schedulers.io())
        .map(x -> supplier.get()).onBackpressureDrop().observeOn(Schedulers.computation())
        .onBackpressureDrop().share();
  }

  /**
   * Generate a Flowable from a periodical call to a Supplier.
   *
   * @param <T> the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier) {
    return toFlow(supplier, 100, TimeUnit.MILLISECONDS);
  }

  public static <T> Flowable<T> toFlowFast(Supplier<T> supplier) {
    return toFlow(supplier, 50, TimeUnit.MILLISECONDS);
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
  public static Function<Double, Double> deadbandMap(double minimum, double maximum,
      double remap) {
    return bandMap(minimum, maximum, x -> remap);
  }

  /**
   * Creates a function that will return the input value, unless that value is within the range
   * specified by minimum and maximum. If so, the value will be passed through the remap function.
   */
  public static Function<Double, Double> bandMap(double minimum, double maximum,
      Function<Double, Double> remap) {
    return x -> x < maximum && x > minimum ? remap.apply(x) : x;
  }

  public static <T> T getLastValue(Flowable<T> input) {
    return input.blockingLatest().iterator().next();
  }

  /**
   * Creates a PID Loop Function.
   * DON'T USE EVER
   */
  @Deprecated
  public static FlowableTransformer<Double, Double> veryBadDontUseEverPidLoop(double proportional,
      int integralBuffer,
      double integral,
      double derivative) {
    return controlLoop(proportional, derivative, integral,
        (error) -> error.buffer(integralBuffer, 1)
            .map(list -> list.stream().reduce(0.0, (x, acc) -> x + acc))
            .map(x -> x * integral / integralBuffer));
  }

  /**
   * Exponential average version of PID Loop.
   */
  public static FlowableTransformer<Double, Double> exponentialPidLoop(double proportinal,
      double integralBuffer,
      double integral,
      double derivative) {
    return controlLoop(proportinal, integral, derivative, error -> error.scan(
        (newVal, lastAvg) -> lastAvg * (integralBuffer - 1) / integralBuffer + newVal / integralBuffer));
  }

  /**
   * RegularPD loop with the 0.0 integral stream.
   */
  public static FlowableTransformer<Double, Double> pdLoop(double proportional,
      double derivative) {
    return controlLoop(proportional, derivative, 0.0, error -> Flowable.just(0.0));
  }

  /**
   * Control Loop for PID which
   */
  private static FlowableTransformer<Double, Double> controlLoop(double proportional,
      double derivative,
      double integral,
      Function<Flowable<Double>, Flowable<Double>> errorI) {
    return error -> {
      Flowable<Double> errorP = error.map(x -> x * proportional);
      Flowable<Double> errorD = error.buffer(2, 1)
          .map(last -> last.stream().reduce(0.0, (x, y) -> x - y))
          .map(x -> x * derivative);
      try {
        return Flowable.combineLatest(errorP, errorI.apply(error).map(x -> x * integral), errorD,
            (p, i, d) -> p + i + d);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    };
  }

  public static Function<Double, Double> limitWithin(double minimum, double maximum) {
    return x -> max(min(x, maximum), minimum);
  }

  public static <T> T printId(T flowables) {
    System.out.println(flowables);
    return flowables;
  }

  /**
   * Combines disposable given to this method in order to return them all at once.
   *
   * @param disposables the disposables you desire to combine
   * @return combined disposables.
   */
  public static Disposable combineDisposable(Disposable... disposables) {
    CompositeDisposable cd = new CompositeDisposable();
    Flowable.fromArray(disposables).blockingSubscribe(cd::add);
    return cd;
  }
}
