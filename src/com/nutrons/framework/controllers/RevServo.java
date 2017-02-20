package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.Servo;

public class RevServo extends ServoController {

  private final Servo servo;

  public RevServo(int port) {
    servo = new Servo(port);
  }

  @Override
  public void accept(ServoEvent servoEvent) {
    servoEvent.actOn(this);
  }

  public void setAngle(double angle) {
    this.servo.setAngle(angle);
  }

  public void set(double value) {
    this.servo.set(value);
  }

}
