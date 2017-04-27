package com.nutrons.framework.util;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


public class FlowOperators {

  /**
   * Generate a Flowable from a periodic call to a Supplier. Drops on backpressure.
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T>     the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> supplier,
                                       long ignored, TimeUnit unit) {
    ConnectableFlowable<T> toRet = Flowable.interval(ignored, unit, Schedulers.io())
        .map(x -> supplier.get()).onBackpressureDrop().observeOn(Schedulers.computation()).publish();
    toRet.connect();
    return toRet;
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
   * 0 if in deadband else f(x-d)
   */
  public static Function<Double, Double> deadbandMap(double minimum, double maximum,
                                                     double remap) {
    return bandMap(minimum, maximum, x -> remap);
  }

  public static Function<Double, Double> deadbandAssign(double deadzone, double rampRate) {
    return (Double x) -> {
      if (x <= -(deadzone / 2)) {
        return -Math.pow(Math.abs(x + deadzone / 2) / (1 - deadzone / 2), rampRate) + 0.2;
      } else if (x >= (deadzone / 2)) {
        return Math.pow(x - deadzone / 2, rampRate) / (1 - deadzone / 2) - 0.2;
      } else {
        return 0.0;
      }
    };
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
   */
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
          (p, i, d) -> p + i + d).publish().autoConnect();
      return output;
    };
  }

  public static FlowableTransformer<Double, Double> pdLoop(double proportional,
                                                           double derivative) {
    return error -> {
      Flowable<Double> errorP = error.map(x -> x * proportional);
      Flowable<Double> errorD = error.buffer(2, 1)
          .map(last -> last.stream().reduce(0.0, (x, y) -> x - y))
          .map(x -> x * derivative);
      return Flowable.combineLatest(errorP, errorD, (p, d) -> p + d).publish().autoConnect();
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
