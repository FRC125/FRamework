package com.nutrons.framework.consumers;

import io.reactivex.functions.Consumer;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController implements Consumer<ControllerEvent> {
  private final int port;

  public LoopSpeedController(int port) {
    this.port = port;
  }

  @Override
  public void accept(ControllerEvent controllerEvent) {
    controllerEvent.actOn(this);
  }

  public int port() {
    return port;
  }
}
