package com.nutrons.framework.controllers;

import com.ctre.CANTalon.TalonControlMode;
import java.security.InvalidParameterException;

/**
 * Created by krzw9 on 1/31/2017.
 */
public enum CanControlMode {
  Speed, Power, Position, Follower;

  public static TalonControlMode toTalonMode(CanControlMode mode){
    switch (mode){
      case Follower:
        return TalonControlMode.Follower;
      case Power:
        return TalonControlMode.PercentVbus;
      case Position:
        return TalonControlMode.Position;
      case Speed:
        return TalonControlMode.Speed;
      default:
        throw new InvalidParameterException();
    }
  }
}
