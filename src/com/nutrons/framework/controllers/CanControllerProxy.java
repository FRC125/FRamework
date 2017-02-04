package com.nutrons.framework.controllers;

/**
 * Created by krzw9 on 1/31/2017.
 */
public interface CanControllerProxy {

  void set(double value);

  double get();

  void setPid(double pval, double ival, double dval);

  void setF(double fval);

  void setSetpoint(double setpoint);

  int getDeviceId();

  void changeControlMode(CanControlMode mode);

  double getError();
}
