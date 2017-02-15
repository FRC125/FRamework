package com.nutrons.framework.controllers;

public class SetReverseSensorEvent implements ControllerEvent {
  private final boolean flipped;

  public SetReverseSensorEvent(boolean flipped) {
    this.flipped = flipped;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setReverseSensor(flipped);
  }
}
