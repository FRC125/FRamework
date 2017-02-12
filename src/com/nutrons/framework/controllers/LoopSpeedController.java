package com.nutrons.framework.controllers;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Provides a common interface for speed controllers that have closed loop capabilities.
 */
public abstract class LoopSpeedController implements Consumer<ControllerEvent> {
  public abstract Flowable<FeedbackEvent> feedback();

  @Override
  public abstract void accept(ControllerEvent event);

  public void setPID(double pval, double ival, double dval, double fval) {
    this.accept(Events.pid(pval, ival, dval, fval));
  }

  public void setControlMode(ControlMode mode) {
    this.accept(Events.mode(mode));
  }

  public void runAtPower(double power) {
    this.accept(Events.power(power));
  }

  public void follow(LoopSpeedController leader) {
    this.accept(Events.follow(leader));
  }

  public void setSetpoint(double setpoint) {
    this.accept(Events.setpoint(setpoint));
  }

  public abstract void setOutputFlipped(boolean flipped);
}
