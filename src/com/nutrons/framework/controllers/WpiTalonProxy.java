package com.nutrons.framework.controllers;

import static com.ctre.CANTalon.TalonControlMode;

import com.ctre.CANTalon;

/**
 * Created by krzw9 on 1/31/2017.
 */
public class WpiTalonProxy implements CanControllerProxy {

  private CANTalon talon;

  public WpiTalonProxy(CANTalon talon) {
    this.talon = talon;
  }

  @Override
  public void set(double value) {
    this.talon.set(value);
  }

  @Override
  public double get() {
    return this.talon.get();
  }

  @Override
  public void setPid(double pval, double ival, double dval) {
    this.talon.setPID(pval, ival, dval);
  }

  @Override
  public void setF(double fval) {
    this.talon.setF(fval);
  }

  @Override
  public void setSetpoint(double setpoint) {
    this.talon.setSetpoint(setpoint);
  }

  @Override
  public int getDeviceId() {
    return this.talon.getDeviceID();
  }

  @Override
  public void changeControlMode(ControlMode mode) {
    TalonControlMode talonMode = TalonControlMode.PercentVbus;
    switch (mode) {
      case FOLLOWER:
        talonMode = TalonControlMode.Follower;
        break;
      case LOOP_POSITION:
        talonMode = TalonControlMode.Position;
        break;
      case LOOP_SPEED:
        talonMode = TalonControlMode.Follower;
        break;
      case MANUAL:
        talonMode = TalonControlMode.PercentVbus;
        break;
      default:
        break;
    }
    this.talon.changeControlMode(talonMode);
  }

  @Override
  public double getError() {
    return this.getError();
  }
}
