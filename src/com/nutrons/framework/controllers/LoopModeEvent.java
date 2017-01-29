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
        talon.changeControlMode(TalonControlMode.PercentVbus);
        break;
      case LOOP_POSITION:
        talon.changeControlMode(TalonControlMode.Position);
        break;
      case LOOP_SPEED:
        talon.changeControlMode(TalonControlMode.Speed);
        break;
    }
  }
}
