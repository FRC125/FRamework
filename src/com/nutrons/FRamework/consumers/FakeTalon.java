package com.nutrons.FRamework.consumers;

import com.ctre.CANTalon;

public class FakeTalon extends Talon {
  private final int port;

  public FakeTalon(int port) {
    super(null);
    this.port = port;
  }

  @Override
  public void accept(LoopControllerEvent motorEvent) {
    super.accept(motorEvent);
  }

  @Override
  public void set(double value) {
    log("setting value to: " + value);
  }

  @Override
  public void setMode(CANTalon.TalonControlMode mode) {
    log("switching mode to: " + mode.name());
  }

  @Override
  public void setLoopProperties(double setpoint, double p, double i, double d, double f) {
    log("setting pidf to: " + p + "," + i + "," + d + "," + f);
    log("settings setpoint to: " + setpoint);
  }

  private void log(Object s) {
    System.out.println("[T" + port + "] " + s.toString());
  }
}
