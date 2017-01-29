package com.nutrons.framework.controllers;

public interface ControllerEvent {
  /**default void actOn(LoopSpeedController controller) {
    throw new EventUnimplementedException(
        controller.getClass().toString(), this.getClass().toString());
  }**/
  default void actOn(Talon t) {
    System.out.println("asdf");
  }
}
