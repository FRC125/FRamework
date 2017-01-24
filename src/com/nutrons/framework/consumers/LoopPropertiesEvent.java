package com.nutrons.framework.consumers;

public class LoopPropertiesEvent implements ControllerEvent {

  private final double kP;
  private final double kI;
  private final double kD;
  private final double kF;
  private final double setpoint;

  /**
   * Sets the coefficients for a closed loop controller.
   */
  public LoopPropertiesEvent(double setpoint, double kP, double kI, double kD, double kF) {
    this.setpoint = setpoint;
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
  }

  public void actOn(Talon talon) {
    talon.setLoopProperties(setpoint, kP, kI, kD, kF);
  }
}
