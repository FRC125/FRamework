package com.nutrons.FRamework.consumers;

public class LoopPropertiesEvent implements LoopControllerEvent {

  private final double kP;
  private final double kI;
  private final double kD;
  private final double kF;

  public LoopPropertiesEvent(double kP, double kI, double kD, double kF) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setLoopProperties(kP, kI, kD, kF);
  }
}
