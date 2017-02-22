package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.Servo;

/**
 * The RevServo controlle used for the programming and running of Rev Smart Servos.
 */
public class RevServo extends ServoController {

  private final Servo servo;

  /**
   * Makes a RevServo object at the given port.
   * @param port Port of RevServo
   */
  public RevServo(int port) {
    servo = new Servo(port);
  }

  @Override
  public void accept(ServoInstr servoInstr) {
    servoInstr.actOn(this);
  }

  /**
   * Sets the Servo to a given angle ranging from -90 to 90 left and right reflectively.
   * @param angle given angle to turn to.
   */
  public void setAngle(double angle) {
    this.servo.setAngle(angle);
  }

  /**
   * Set the motor to a position given a value 0.0 is full left 1.0 is full right.
   * @param value The value to turn the servo to.
   */
  public void set(double value) {
    this.servo.set(value);
  }

}
