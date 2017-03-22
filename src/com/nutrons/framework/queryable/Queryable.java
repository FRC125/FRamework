package com.nutrons.framework.queryable;

import io.reactivex.Flowable;
import java.util.function.Supplier;

public interface Queryable<T> {
  T get();
  Flowable<T> getFlowable();

  static <T> Queryable fromSupplier(Supplier<T> supplier){
    return new QueryableFromSupplier(supplier);
  }
}
