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
    switch (this.mode) {
      case MANUAL:
        talon.changeControlMode(ControlMode.MANUAL);
        break;
      case LOOP_POSITION:
        talon.changeControlMode(ControlMode.LOOP_POSITION);
        break;
      case LOOP_SPEED:
        talon.changeControlMode(ControlMode.LOOP_SPEED);
        break;
    }
  }
}
