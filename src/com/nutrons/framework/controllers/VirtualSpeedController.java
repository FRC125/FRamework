package com.nutrons.framework.controllers;

import io.reactivex.Flowable;

public class VirtualSpeedController extends LoopSpeedController {
  @Override
  public Flowable<FeedbackEvent> feedback() {
    return Flowable.empty();
  }

  @Override
  public void accept(ControllerEvent controllerEvent) {
    controllerEvent.actOn(this);
  }

  @Override
  public void setOutputFlipped(boolean flipped) {

  }
}
