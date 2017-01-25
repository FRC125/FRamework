package com.nutrons.framework.consumers;

import com.ctre.CANTalon;

public class FollowerTalon extends Talon {
  /**
   * A talon which will automatically follow another talon on creation.
   * @param talonPort the port which this talon operates on
   * @param targetToFollow the target talon to follow
   */
  public FollowerTalon(int talonPort, double targetToFollow) {
    super(talonPort);
    super.changeControlMode(CANTalon.TalonControlMode.Follower);
    super.set(targetToFollow);
  }
}
