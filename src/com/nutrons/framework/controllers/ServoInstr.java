package com.nutrons.framework.controllers;

public interface ServoInstr {

  default void actOn(RevServo servo) {
    throw new EventUnimplementedException(
        servo.getClass().toString(), this.getClass().toString());
  }

}
