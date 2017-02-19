package com.nutrons.framework.util;

import java.util.function.Supplier;

public class IntervalCache<T> implements Supplier<T> {

  private final long interval;
  private final Supplier<T> supplier;
  private volatile long lastTimeRead;
  private volatile T lastValue;

  public IntervalCache(long interval, Supplier<T> supplier) {
    this.interval = interval;
    this.supplier = supplier;
    update();
  }

  private synchronized T update() {
    if (needUpdate()) {
      this.lastTimeRead = System.currentTimeMillis();
      this.lastValue = supplier.get();
    }
    return lastValue;
  }

  private boolean needUpdate() {
    return System.currentTimeMillis() - this.lastTimeRead > interval;
  }

  @Override
  public T get() {
    if (needUpdate()) {
      return update();
    } else {
      return lastValue;
    }
  }
}
