package com.nutrons.framework.controllers;

public interface ControllerEvent {

  default void actOn(Talon talon) {
    throw new EventUnimplementedException(
        talon.getClass().toString(), this.getClass().toString());
  }
}
