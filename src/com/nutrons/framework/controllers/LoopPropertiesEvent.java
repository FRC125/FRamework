package com.nutrons.framework.controllers;

public class LoopPropertiesEvent implements ControllerEvent {

  private final double pval;
  private final double ival;
  private final double dval;
  private final double fval;

  /**
   * Sets the coefficients for a closed loop controller.
   */
  public LoopPropertiesEvent(double pval,
      double ival,
      double dval,
      double fval) {
    this.pval = pval;
    this.ival = ival;
    this.dval = dval;
    this.fval = fval;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setP(pval);
    talon.setI(ival);
    talon.setD(dval);
    talon.setF(fval);
  }

  @Override
  public void actOn(VirtualSpeedController controller) {
    controller.setLoopProperties(pval, ival, dval, fval);
  }
}
