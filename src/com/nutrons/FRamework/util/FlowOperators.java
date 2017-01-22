package com.nutrons.FRamework.util;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class FlowOperators {
  /**
   * Generate a Flowable from a periodic call to a Supplier.
   *
   * @param ignored the number of time units to wait before calling the supplier again
   * @param <T>     the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> s, long ignored, TimeUnit unit) {
    return Flowable.interval(ignored, unit, Schedulers.io())
        .subscribeOn(Schedulers.io()).map((x) -> s.get());
  }

  /**
   * Generate a Flowable from a periodical call to a Supplier.
   *
   * @param <T> the type of the Flowable and Supplier
   */
  public static <T> Flowable<T> toFlow(Supplier<T> s) {
    return toFlow(s, 100, TimeUnit.MILLISECONDS);
  }
}
