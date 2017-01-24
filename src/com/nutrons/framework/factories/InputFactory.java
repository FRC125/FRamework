package com.nutrons.framework.factories;

import io.reactivex.Flowable;

public interface InputFactory {
  Flowable<Double> controllerX(int instance);

  Flowable<Double> controllerX2(int instance);

  Flowable<Double> controllerY(int instance);

  Flowable<Double> controllerY2(int instance);
}
