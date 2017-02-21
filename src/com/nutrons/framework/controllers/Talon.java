package com.nutrons.framework.controllers;

import com.ctre.CANTalon;
import io.reactivex.Flowable;

import java.security.InvalidParameterException;

import static com.nutrons.framework.util.FlowOperators.toFlow;

public class Talon extends LoopSpeedController {

  private final Flowable<FeedbackEvent> feedback;
  private final CANTalon talon;
  private boolean reverseFeedback;

  /**
   * Creates a talon on the given port.
   */
  public Talon(int port) {
    this(new CANTalon(port));
  }

  public Talon(CANTalon talon) {
    this.talon = talon;
    this.feedback = toFlow(() -> this.talon::getError);
  }

  public Talon(int port, CANTalon.FeedbackDevice feedbackDevice) {
    this(port);
    this.talon.setFeedbackDevice(feedbackDevice);
  }

  /**
   * Creates a talon that initially follows another talon.
   *
   * @param toFollow the talon to follow
   */
  public Talon(int port, Talon toFollow) {
    this(port);
    this.changeControlMode(ControlMode.FOLLOWER);
    this.set(toFollow.id());
  }

  void set(double value) {
    this.talon.set(value);
  }

  void changeControlMode(ControlMode mode) {
    switch (mode) {
      case FOLLOWER:
        this.talon.changeControlMode(CANTalon.TalonControlMode.Follower);
        break;
      case LOOP_POSITION:
        this.talon.changeControlMode(CANTalon.TalonControlMode.Position);
        break;
      case LOOP_SPEED:
        this.talon.changeControlMode(CANTalon.TalonControlMode.Speed);
        break;
      case MANUAL:
        this.talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        break;
      default:
        throw new InvalidParameterException("This ControlMode is not supported!");
    }
  }

  void changeSetpoint(double setpoint) {
    this.talon.setSetpoint(setpoint);

  }

  void setP(double pval) {
    this.talon.setP(pval);
  }

  void setI(double ival) {
    this.talon.setI(ival);
  }

  void setD(double dval) {
    this.talon.setD(dval);
  }

  void setF(double fval) {
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

  @Override
  public void setOutputFlipped(boolean flipped) {
    talon.setInverted(flipped);
  }

  @Override
  public double speed() {
    return this.talon.getSpeed();
  }

  public void setFeedbackDevice(CANTalon.FeedbackDevice device) {
    this.talon.setFeedbackDevice(device);
  }

  void reverseSensor(boolean flipped) {
    talon.reverseSensor(flipped);
  }

  int id() {
    return this.talon.getDeviceID();
  }

  void setOutputVoltage(double min, double max) {
    this.talon.configNominalOutputVoltage(Math.max(min, 0.0), Math.min(max, 0.0));
    this.talon.configPeakOutputVoltage(Math.max(max, 0.0), Math.min(min, 0.0));
  }

  @Override
  public double position() {
    return this.talon.getPosition();
  }

  void resetPositionTo(double position) {
    this.talon.setPosition(position);
  }


  @Override
  public boolean fwdLimitSwitchClosed(){
    return this.talon.isFwdLimitSwitchClosed();
  }

  @Override
  public boolean revLimitSwitchClosed() {
    return this.talon.isRevLimitSwitchClosed();
  }

  public double getClosedLoopError() {
    return this.talon.getClosedLoopError();
  }
}
