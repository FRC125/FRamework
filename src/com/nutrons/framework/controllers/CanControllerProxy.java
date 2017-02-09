package com.nutrons.framework.controllers;

<<<<<<< HEAD
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
=======
import edu.wpi.first.wpilibj.CANSpeedController;
>>>>>>> master

public interface CanControllerProxy {

  void set(double value);

  double get();

  void setPid(double pval, double ival, double dval);

  void setF(double fval);

  void setSetpoint(double setpoint);

  int getDeviceId();

  void changeControlMode(ControlMode mode);

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
