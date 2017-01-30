package com.nutrons.framework.controllers;

public class LoopPropertiesEvent implements ControllerEvent {

  private final double pval;
  private final double ival;
  private final double dval;
  private final double fval;
  private final double setpoint;

  /**
   * Sets the coefficients for a closed loop controller.
   */
  public LoopPropertiesEvent(double setpoint, double pval,
                             double ival, double dval, double fval) {
    this.setpoint = setpoint;
    this.pval = pval;
    this.ival = ival;
    this.dval = dval;
    this.fval = fval;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setLoopProperties(setpoint, pval, ival, dval, fval);
  }
}
