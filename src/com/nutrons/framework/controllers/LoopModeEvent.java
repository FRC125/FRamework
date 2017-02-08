package com.nutrons.framework.controllers;

public class LoopModeEvent implements ControllerEvent {

  private final ControllerMode mode;

  public LoopModeEvent(ControllerMode mode) {
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
        talon.changeControlMode(CanControlMode.Power);
        break;
      case LOOP_POSITION:
        talon.changeControlMode(CanControlMode.Position);
        break;
      case LOOP_SPEED:
        talon.changeControlMode(CanControlMode.Speed);
        break;
      default:
        talon.changeControlMode(CanControlMode.Position);
        break;
    }
  }
}
