package com.nutrons.framework.controllers;

class SetpointEvent implements ControllerEvent {
  private final double setpoint;

  SetpointEvent(double setpoint) {
    this.setpoint = setpoint;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setSetpoint(setpoint);
  }
}
