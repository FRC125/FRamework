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

  /**
   * A consumer which will apply ControllerEvents to this speed controller.
   */
  public Consumer<ControllerEvent> output() {
    return this.consumer;
  }

  /**
   * A stream which contains feedback events produced by this speed controller.
   */
  public abstract Flowable<FeedbackEvent> feedback();
}
