package com.nutrons.framework.controllers;

import com.ctre.CANTalon;
import io.reactivex.Flowable;

import java.security.InvalidParameterException;

import static com.nutrons.framework.util.FlowOperators.toFlow;

public class Talon extends LoopSpeedController {

  private final Flowable<FeedbackEvent> feedback;
  private final CANTalon talon;

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
  public void resetPositionTo(double position) {

  }

  @Override
  public double position() {
    return 0;
  }

  @Override
  public void setOutputVoltage(double min, double max) {

  }

  @Override
  public void accept(ControllerEvent event) {
    event.actOn(this);
  }

  int id() {
    return this.talon.getDeviceID();
  }
}
