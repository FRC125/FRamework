package com.nutrons.framework.controllers;

public class OutputVoltageEvent implements ControllerEvent {

  private final double min;
  private final double max;

  public OutputVoltageEvent(double min, double max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Set min and max voltage output of Talon object
   *
   * @param talon Talon controller to apply event to.
   */
  public void actOn(Talon talon) {
    talon.setOutputVoltage(min, max);
  }
}
