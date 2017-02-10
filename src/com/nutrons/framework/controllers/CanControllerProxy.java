package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.CANSpeedController;

public interface CanControllerProxy {

  void set(double value);

  double get();

  void setPid(double pval, double ival, double dval);

  void setF(double fval);

  void setSetpoint(double setpoint);

  int getDeviceId();

  void changeControlMode(ControlMode mode);

  double getError();
}
