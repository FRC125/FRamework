package com.nutrons.framework.controllers;

import io.reactivex.Flowable;

public class VirtualSpeedController extends LoopSpeedController {
  @Override
  public Flowable<FeedbackEvent> feedback() {
    return Flowable.empty();
  }

  @Override
  public void resetPositionTo(double position) {

  }

  @Override
  public double position() {
    return 0;
  }

  @Override
  public void setOutputVoltage(double min, double max) {

  }

  @Override
  public void accept(ControllerEvent controllerEvent) {
    controllerEvent.actOn(this);
  }
}
