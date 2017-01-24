package com.nutrons.framework.consumers;

import com.ctre.CANTalon;

public class RunAtPowerEvent implements ControllerEvent {
  private final double power;

  public RunAtPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  public void actOn(Talon talon) {
    talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    talon.set(power);
  }
}
