package com.nutrons.framework.controllers;

public class LoopModeEvent implements ControllerEvent {

  private final ControlMode mode;

  public LoopModeEvent(ControlMode mode) {
    this.mode = mode;
  }

  /**
   * Visit a Talon object.
   *
   * @param talon Talon controller to apply event to.
   */
  public void actOn(Talon talon) {
    talon.changeControlMode(mode);
  }
}
