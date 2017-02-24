package com.nutrons.framework.controllers;

public class ServoInstr {

  public static ServoCommand setAngle(double angle) {
    return new SetAngleCommand(angle);
  }

  public static ServoCommand set(double value) {
    return new SetCommand(value);
  }

}
