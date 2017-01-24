package com.nutrons.framework.consumers;

import com.ctre.CANTalon;

public class Talon extends LoopSpeedController {
  private CANTalon talon;

  public Talon(int port) {
    this.talon = new CANTalon(port);
  }

  @Override
  public void accept(ControllerEvent motorEvent) {
    motorEvent.actOn(this);
  }

  void set(double value) {
    this.talon.set(value);
  }

  void changeControlMode(CANTalon.TalonControlMode mode) {
    this.talon.changeControlMode(mode);
  }

  void setLoopProperties(double setpoint, double p, double i, double d, double f) {
    this.talon.setSetpoint(setpoint);
    this.talon.setPID(p, i, d);
    this.talon.setF(f);
  }
}
