package com.nutrons.framework.controllers;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import com.ctre.CANTalon;
import com.nutrons.framework.util.FlowOperators;
import io.reactivex.Flowable;

public class Talon extends LoopSpeedController {

  private final Flowable<FeedbackEvent> feedback;
  private final CanControllerProxy talon;

  /**
   * Creates a talon on the given port.
   */
  public Talon(int port) {
    this(new WpiTalonProxy(new CANTalon(port)));
  }

  public Talon(CanControllerProxy talon) {
    this.talon = talon;
    this.feedback = toFlow(() -> () -> this.talon.getError());
  }

  /**
   * Creates a talon that initially follows another talon.
   *
   * @param toFollow the talon to follow
   */
  public Talon(int port, Talon toFollow) {
    this(port);
    this.changeControlMode(CanControlMode.Follower);
    this.set(toFollow.id());
  }

  void set(double value) {
    this.talon.set(value);
  }

  void changeControlMode(CanControlMode mode) {
    this.talon.changeControlMode(mode);
  }

  void setLoopProperties(double setpoint, double pval,
      double ival, double dval, double fval) {
    this.talon.setSetpoint(setpoint);
    this.talon.setPid(pval, ival, dval);
    this.talon.setF(fval);
  }

  @Override
  public Flowable<FeedbackEvent> feedback() {
    return this.feedback;
  }

  @Override
  public void accept(ControllerEvent event) {
    event.actOn(this);
  }

  int id() {
    return this.talon.getDeviceId();
  }

  public double getPosition(){
    return this.talon.getEncoderPosition();
  }

  public double getEncoderPosition() { return this.talon.getEncoderPosition(); }

  public void configEncoderCodesPerRev(int value) { this.talon.configEncoderCodesPerRev(value);}

  public void setFeedbackDevice(CANTalon.FeedbackDevice device){
    this.talon.setFeedbackDevice(device);
  }

  public void setProfile(int profile){
    this.talon.setProfile(profile);
  }

  public void reverseSensor(boolean flip){
    this.talon.reverseSensor(flip);
  }

  public void reverseOutput(boolean flip){
    this.talon.reverseOutput(flip);
  }

  public void setEncPosition(int newPosition){
    this.talon.setEncPosition(newPosition);
  }

  public int getPulseWidthPosition(){
    return this.talon.getPulseWidthPosition();
  }

  public void configNominalOutputVoltage(double forwardVoltage, double reverseVoltage){
    this.talon.configNominalOutputVoltage(forwardVoltage, reverseVoltage);
  }

  public void configPeakOutputVoltage(double forwardVoltage, double reverseVoltage){
    this.talon.configPeakOutputVoltage(forwardVoltage, reverseVoltage);
  }
}
