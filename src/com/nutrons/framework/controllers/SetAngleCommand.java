package com.nutrons.framework.controllers;

public class SetAngleCommand implements ServoCommand {

  private final double angle;

  /**
   * An event which sets the servo to turn to a certain angle.
   *
   * @param angle the angle to turn the servo to -90 (left) to 90 (right).
   */
  public SetAngleCommand(double angle) {
    this.angle = angle;
  }

  public double angle() {
    return this.angle;

  }

  @Override
  public void actOn(RevServo servo) {
    if (Math.abs(angle) > 90.0) {
      throw new IllegalArgumentException(
          "Angle greater than 90 degrees is not supported for Servos");
    }
    servo.setAngle(angle);

  }

}
