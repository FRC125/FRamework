package com.nutrons.FRamework.consumers;

import com.ctre.CANTalon;

public class RunAtPowerEvent implements LoopControllerEvent {
  private final double power;

  public RunAtPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setMode(CANTalon.TalonControlMode.PercentVbus);
    talon.set(power);
  }
}
