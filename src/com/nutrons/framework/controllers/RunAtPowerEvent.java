package com.nutrons.framework.controllers;

import com.ctre.CANTalon;

public class RunAtPowerEvent implements ControllerEvent {
  private final double power;

  /**
   * An event which sets the controller to manually run at a given power.
   *
   * @param power the power to run the controller at, from -1.0 to 1.0
   */
  public RunAtPowerEvent(double power) {
    this.power = (Math.abs(power) <= 1.0) ? power : 0.0; //Ensures power is between -1.0 and 1.0, otherwise set power to 0.0
  }

  public double power() {
    return this.power;
  }

  @Override
  public void actOn(Talon talon) {
    talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    talon.set(power);
  }
}