package com.nutrons.framework.producers;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import edu.wpi.first.wpilibj.Joystick;
import io.reactivex.Flowable;

/**
 * A wrapper around WPI's "Joysticks" which provides Flowables for Gamepad data.
 */
public class WpiGamepad {
  private final int port;
  private final Flowable<Double> axisX1;
  private final Flowable<Double> axisY1;
  private final Flowable<Double> axisX2;
  private final Flowable<Double> axisY2;
  private final Joystick joystick;

  public WpiGamepad(int port, int axisX1, int axisY1, int axisX2, int axisY2) {
    this.port = port;
    this.joystick = new Joystick(this.port);
    this.axisX1 = toFlow(() -> this.joystick.getRawAxis(axisX1));
    this.axisY1 = toFlow(() -> this.joystick.getRawAxis(axisY1));
    this.axisX2 = toFlow(() -> this.joystick.getRawAxis(axisX2));
    this.axisY2 = toFlow(() -> this.joystick.getRawAxis(axisY2));
  }

  /**
   * A Flowable providing data from the first joystick's x-axis
   **/
  public Flowable<Double> joy1X() {
    return this.axisX1;
  }

  /**
   * A Flowable providing data from the first joystick's y-axis
   **/
  public Flowable<Double> joy2X() {
    return this.axisX2;
  }

  /**
   * A Flowable providing data from the second joystick's x-axis
   **/
  public Flowable<Double> joyY1() {
    return this.axisY1;
  }

  /**
   * A Flowable providing data from the second joystick's y-axis
   **/
  public Flowable<Double> joyY2() {
    return this.axisY2;
  }
}
