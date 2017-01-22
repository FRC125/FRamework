package com.nutrons.FRamework.consumers;

import com.ctre.CANTalon;

public class LoopFollowEvent implements LoopControllerEvent {

  private final int target;

  public LoopFollowEvent(int targetToFollow) {
    this.target = targetToFollow;
  }

  @Override
  public void actOn(Talon talon) {
    talon.setMode(CANTalon.TalonControlMode.Follower);
    talon.set(target);
  }
}
