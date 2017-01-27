package com.nutrons.framework.consumers;

public interface ControllerEvent {
  default void actOn(LoopSpeedController controller) {
    throw new EventUnimplementedException(
        controller.getClass().toString(), this.getClass().toString());
  }
}
