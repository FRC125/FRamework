package com.nutrons.FRamework.consumers;

import static com.ctre.CANTalon.TalonControlMode;

public class LoopModeEvent implements LoopControllerEvent {

  private final TalonControlMode mode;

  public LoopModeEvent(TalonControlMode mode) {
    this.mode = mode;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setMode(this.mode);
  }
}
