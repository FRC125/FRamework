package com.nutrons.framework.controllers;

import com.ctre.CANTalon;

public interface CanControllerProxy {

  void set(double value);

  double get();

  void setPid(double pval, double ival, double dval);

  void setF(double fval);

  void setSetpoint(double setpoint);

  int getDeviceId();

  void changeControlMode(ControlMode mode);

  double getError();

  void resetPositionTo(double position);

  double position();

  void setFeedbackDevice(CANTalon.FeedbackDevice device);

  void setOutputVoltage(double min, double max);
}
