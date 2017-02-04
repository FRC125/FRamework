package com.nutrons.framework.controllers;

import com.ctre.CANTalon;

/**
 * Created by krzw9 on 1/31/2017.
 */
public class WpiTalonProxy implements CanControllerProxy {

  private CANTalon talon;
  public WpiTalonProxy(CANTalon talon){
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
  public void setPID(double pVal, double iVal, double dVal) {
    this.talon.setPID(pVal,iVal,dVal);
  }

  @Override
  public void setF(double fVal) {
    this.talon.setF(fVal);
  }

  @Override
  public void setSetpoint(double setpoint) {
    this.talon.setSetpoint(setpoint);
  }

  @Override
  public int getDeviceID() {
    return this.talon.getDeviceID();
  }

  @Override
  public void changeControlMode(CanControlMode mode) {
    this.talon.changeControlMode(CanControlMode.toTalonMode(mode));
  }

  @Override
  public double getError() {
    return this.getError();
  }
}
