package com.nutrons.framework.controllers;

import static com.ctre.CANTalon.TalonControlMode;

public class LoopModeEvent implements ControllerEvent {
  private final ControllerMode mode;

  public LoopModeEvent(ControllerMode mode) {
    this.mode = mode;
  }

  public void actOn(Talon talon) {
    switch(this.mode) {
      case MANUAL:
        talon.changeControlMode(CanControlMode.Power);
        break;
      case LOOP_POSITION:
        talon.changeControlMode(CanControlMode.Position);
        break;
      case LOOP_SPEED:
        talon.changeControlMode(CanControlMode.Speed);
        break;
    }
  }
}
