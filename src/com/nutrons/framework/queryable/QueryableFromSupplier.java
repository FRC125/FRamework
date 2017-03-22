package com.nutrons.framework.queryable;

import com.nutrons.framework.util.FlowOperators;
import io.reactivex.Flowable;
import java.util.function.Supplier;

public class QueryableFromSupplier<T> implements Queryable<T> {

  private Supplier<T> supplier;

  public QueryableFromSupplier(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  public T get() {
    return supplier.get();
  }

  @Override
  public Flowable getFlowable() {
    return FlowOperators.toFlow(supplier);
  }

}
