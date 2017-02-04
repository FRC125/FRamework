package com.nutrons.framework.controllers;

/**
 * Created by krzw9 on 1/31/2017.
 */
public interface CanControllerProxy {
  void set(double value);
  double get();
  void setPID(double pVal, double iVal, double dVal);
  void setF(double fVal);
  void setSetpoint(double setpoint);
  int getDeviceID();
  void changeControlMode(CanControlMode mode);
  double getError();
}
