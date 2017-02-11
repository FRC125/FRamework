package com.nutrons.framework.controllers;

public interface ControllerEvent {

  default void actOn(Talon talon) {
    throw new EventUnimplementedException(
        talon.getClass().toString(), this.getClass().toString());
  }

  default void actOn(VirtualSpeedController controller) {
    throw new EventUnimplementedException(
        controller.getClass().toString(), this.getClass().toString());
  }
}
