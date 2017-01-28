package com.nutrons.framework.controllers;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import com.ctre.CANTalon;
import io.reactivex.Flowable;

public class Talon extends LoopSpeedController {
  private final Flowable<FeedbackEvent> feedback;
  private CANTalon talon;

  public Talon(int port) {
    this.talon = new CANTalon(port);
    this.feedback = toFlow(() -> () -> this.talon.getError());
  }

  void set(double value) {
    this.talon.set(value);
  }

  void changeControlMode(CANTalon.TalonControlMode mode) {
    this.talon.changeControlMode(mode);
  }

  void setLoopProperties(double setpoint, double pval,
                         double ival, double dval, double fval) {
    this.talon.setSetpoint(setpoint);
    this.talon.setPID(pval, ival, dval);
    this.talon.setF(fval);
  }

  @Override
  public Flowable<FeedbackEvent> feedback() {
    return this.feedback;
  }
}
