package com.nutrons.FRamework.consumers;

public class SetPowerEvent implements MotorEvent {
  private final double power;

  public SetPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  @Override
  public void actOn(LoopSpeedController controller) {
    controller.setRunAtPower(this.power);
  }
}
