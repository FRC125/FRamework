package com.nutrons.framework.controllers;
import com.ctre.CANTalon.FeedbackDevice;

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
    return this.talon.getError();
  }

  @Override
  public double getPosition() {
    return this.talon.getPosition();
  }

  @Override
  public double getEncoderPosition() { return this.talon.getEncPosition(); }

  @Override
  public void configEncoderCodesPerRev(int value) { this.talon.configEncoderCodesPerRev(value);}

  @Override
  public void setFeedbackDevice(CANTalon.FeedbackDevice device){
    this.talon.setFeedbackDevice(device);
  }

  @Override
  public void setProfile(int profile){
    this.talon.setProfile(profile);
  }

  @Override
  public void reverseSensor(boolean flip){
    this.talon.reverseSensor(flip);
  }

  @Override
  public void reverseOutput(boolean flip){
    this.talon.reverseOutput(flip);
  }

  @Override
  public void setEncPosition(int newPosition){
    this.talon.setEncPosition(newPosition);
  }

  @Override
  public int getPulseWidthPosition(){
    return this.talon.getPulseWidthPosition();
  }

  @Override
  public void configNominalOutputVoltage(double forwardVoltage, double reverseVoltage){
    this.talon.configNominalOutputVoltage(forwardVoltage, reverseVoltage);
  }

  @Override
  public void configPeakOutputVoltage(double forwardVoltage, double reverseVoltage){
    this.talon.configPeakOutputVoltage(forwardVoltage, reverseVoltage);
  }
}
