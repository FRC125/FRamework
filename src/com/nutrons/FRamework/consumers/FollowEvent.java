package com.nutrons.FRamework.consumers;

import com.ctre.CANTalon;

public class FollowEvent implements ControllerEvent {

  private final int target;

  public FollowEvent(int targetToFollow) {
    this.target = targetToFollow;
  }

  public void actOn(Talon talon) {
    talon.changeControlMode(CANTalon.TalonControlMode.Follower);
    talon.set(target);
  }
}
