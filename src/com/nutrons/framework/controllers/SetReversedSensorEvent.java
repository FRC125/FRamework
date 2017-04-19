package com.nutrons.framework.controllers;

public class SetReversedSensorEvent implements ControllerEvent {

  private final boolean flipped;

  public SetReversedSensorEvent(boolean flipped) {
    this.flipped = flipped;
  }

  @Override
  public void actOn(Talon talon) {
    talon.reverseSensor(flipped);
  }

  @Override
  public void actOn(VirtualSpeedController controller) {
    controller.reverseSensor(true);
  }
}
