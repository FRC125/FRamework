package com.nutrons.framework.controllers;

import io.reactivex.functions.Consumer;

public abstract class ServoController implements Consumer<ServoEvent> {

  @Override
  public abstract void accept(ServoEvent servoEvent);

  public void setAngle(double angle) { this.accept(Events.setAngle(angle)); }

  public void set(double value) { this.accept(Events.set(value)); }
}
