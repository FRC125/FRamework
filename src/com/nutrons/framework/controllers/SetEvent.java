package com.nutrons.framework.controllers;

public class SetEvent implements ServoEvent {

  private final double value;

  /**
   * An event which sets the servo to turn to a certain angle.
   *
   * @param value the input value to give the servo -1 is full left 1 is full right.
   */
  public SetEvent(double value) {
    this.value = value;

  }

  public double value() {
    return this.value;

  }

  @Override
  public void actOn(RevServo servo) {
    if (Math.abs(value) > 1.0) {
      throw new EventUnimplementedException(
          "Value input greater than 1.0 is not supported for this servo");

    }
    servo.set(value);

  }

}
