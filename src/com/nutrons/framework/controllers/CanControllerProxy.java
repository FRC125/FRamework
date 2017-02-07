package com.nutrons.framework.controllers;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

public interface CanControllerProxy {

  void set(double value);

  double get();

  void setPid(double pval, double ival, double dval);

  void setF(double fval);

  void setSetpoint(double setpoint);

  int getDeviceId();

  void changeControlMode(CanControlMode mode);

  double getError();

  double getPosition();

  double getEncoderPosition();

  void configEncoderCodesPerRev(int value);

  void setFeedbackDevice(CANTalon.FeedbackDevice device);

  void setProfile(int profile);

  void reverseSensor(boolean flip);

  void reverseOutput(boolean flip);

  void setEncPosition(int newPosition);

  int getPulseWidthPosition();

  void configNominalOutputVoltage(double forwardVoltage, double reverseVoltage);

  void configPeakOutputVoltage(double forwardVoltage, double reverseVoltage);
}
