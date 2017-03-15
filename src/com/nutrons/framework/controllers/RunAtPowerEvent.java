package com.nutrons.framework.controllers;

public class RunAtPowerEvent implements ControllerEvent {

  private final double power;

  /**
   * An event which sets the controller to manually run at a given power.
   *
   * @param power the power to run the controller at, from -1.0 to 1.0
   */
  public RunAtPowerEvent(double power) {
    this.power = power;
  }

  public double power() {
    return this.power;
  }

  @Override
  public void actOn(Talon talon) {
    talon.changeControlMode(ControlMode.MANUAL);
    if (Math.abs(power) > 1.0) {
      throw new EventUnimplementedException(
          "Power greater than magnitude of 1.0 is not supported for Talons");
    }
    talon.set(power);
  }

  public void actOn(VirtualSpeedController controller) {
    controller.setControlMode(ControlMode.MANUAL);
    controller.setRawOutput(power);
  }
}
