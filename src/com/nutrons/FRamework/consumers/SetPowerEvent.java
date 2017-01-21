package com.nutrons.FRamework.consumers;

public class SetPowerEvent implements LoopControllerEvent {
  private final double power;

  public SetPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  @Override
  public void actOn(LoopSpeedController controller) {
    controller.runAtPower(this.power);
  }
}
