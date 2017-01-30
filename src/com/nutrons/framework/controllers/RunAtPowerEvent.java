package com.nutrons.framework.controllers;

import com.ctre.CANTalon;

public class RunAtPowerEvent implements ControllerEvent {
  private final double power;

  /**
   * An event which sets the controller to manually run at a given power.
   * @param power the power to run the controller at, from -1.0 to 1.0
   */
  public RunAtPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  // TODO make sure Talons use power from -1.0 to 1.0, then force this requirement in the constructor.
  @Override
  public void actOn(Talon talon) {
    talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    talon.set(power);
  }
}
