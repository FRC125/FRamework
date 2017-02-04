package com.nutrons.framework.controllers;

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
