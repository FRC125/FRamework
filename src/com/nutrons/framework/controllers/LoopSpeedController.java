package com.nutrons.framework.controllers;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController {
  private final Consumer<ControllerEvent> consumer;

  public LoopSpeedController() {
    this.consumer = x -> x.actOn(this);
  }

  public Consumer<ControllerEvent> consumer() {
    return this.consumer;
  }

  public abstract Flowable<FeedbackEvent> feedback();
}
