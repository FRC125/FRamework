package com.nutrons.framework.controllers;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import com.ctre.CANTalon;
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
}
