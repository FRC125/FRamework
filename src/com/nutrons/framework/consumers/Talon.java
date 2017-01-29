package com.nutrons.framework.consumers;

import com.ctre.CANTalon;

public class Talon extends LoopSpeedController {
  private CANTalon talon;

  public Talon(int port) {
    super(port);
    this.talon = new CANTalon(port);
  }

  void set(double value) {
    this.talon.set(value);
  }

  void changeControlMode(CANTalon.TalonControlMode mode) {
    this.talon.changeControlMode(mode);
  }

  void setLoopProperties(double setpoint, double pval,
                         double ival, double dval, double fval) {
    this.talon.setSetpoint(setpoint);
    this.talon.setPID(pval, ival, dval);
    this.talon.setF(fval);
  }

  public double getDeviceID(){ return this.talon.getDeviceID(); }
}