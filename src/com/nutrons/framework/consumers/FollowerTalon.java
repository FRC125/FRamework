package com.nutrons.framework.consumers;

import com.ctre.CANTalon;

public class FollowerTalon extends Talon {
  public FollowerTalon(int talonPort, double targetToFollow) {
    super(talonPort);
    super.changeControlMode(CANTalon.TalonControlMode.Follower);
    super.set(targetToFollow);
  }
}
