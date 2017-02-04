package com.nutrons.framework.controllers;

import com.ctre.CANTalon;

public class RunAtPowerEvent implements ControllerEvent {
  private final double power;

  /**
   * An event which sets the controller to manually run at a given power.
   */
  public RunAtPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  @Override
  public void actOn(Talon talon) {
    if(Math.abs(power) > 1.0) {
        throw new EventUnimplementedException("Power greater than magnitude of 1.0 is not supported for Talons");
    }
    talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    talon.set(power);
  }
}