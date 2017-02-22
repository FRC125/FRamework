package com.nutrons.framework.controllers;

import io.reactivex.functions.Consumer;

public abstract class ServoController implements Consumer<ServoInstr> {

  @Override
  public abstract void accept(ServoInstr servoInstr);

  public void setAngle(double angle) { this.accept(Events.setAngle(angle)); }

  public void set(double value) { this.accept(Events.set(value)); }
}
