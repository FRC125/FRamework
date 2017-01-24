package com.nutrons.framework.consumers;

import io.reactivex.functions.Consumer;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController implements Consumer<ControllerEvent> {
  @Override
  public void accept(ControllerEvent controllerEvent) {
    controllerEvent.actOn(this);
  }
}
