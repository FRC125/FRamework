package com.nutrons.framework.controllers;

import io.reactivex.functions.Consumer;

public abstract class ServoController implements Consumer<ServoCommand> {

  @Override
  public abstract void accept(ServoCommand servoCommand);

  public void setAngle(double angle) {
    this.accept(ServoInstr.setAngle(angle));
  }

  public void set(double value) {
    this.accept(ServoInstr.set(value));
  }
}
