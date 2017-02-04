package com.nutrons.framework.controllers;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController implements Consumer<ControllerEvent> {

  public abstract Flowable<FeedbackEvent> feedback();
}
