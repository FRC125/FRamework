package com.nutrons.framework.controllers;

public class Events {

  /**
   * Initiate a
   * <a href="https://en.wikipedia.org/wiki/PID_controller">PID</a> loop
   *
   * @param setPoint target location or velocity of PID loop
   * @param pval proportion value of PID
   * @param ival integral value of PID
   * @param dval derivative value of PID
   * @param fval offset value of PID
   * @return a ControllerEvent that initiates a PID loop on the motor controller
   */
  public static ControllerEvent pid(double setPoint,
      double pval,
      double ival,
      double dval,
      double fval) {
    ControllerEvent setpointEvent = new SetpointEvent(setPoint);
    ControllerEvent pid = pid(pval, dval, ival, fval);
    return new ControllerEvent() {
      @Override
      public void actOn(Talon talon) {
        pid.actOn(talon);
        setpointEvent.actOn(talon);
      }
    };
  }

  public static ControllerEvent pid(double pval, double ival, double dval, double fval) {
    return new LoopPropertiesEvent(pval, ival, dval, fval);
  }

  /**
   * Combines actions that are preformed on the talon.
   * @return combined event
   */
  public static ControllerEvent combine(ControllerEvent... events) {
    return new ControllerEvent() {
      @Override
      public void actOn(Talon talon) {
        for (ControllerEvent e : events) {
          e.actOn(talon);
        }
      }
    };
  }

  public static ControllerEvent setReversedSensor(boolean flipped) {
    return new SetReversedSensorEvent(flipped);

  }

  public static ControllerEvent setpoint(double setpoint) {
    return new SetpointEvent(setpoint);
  }

  /**
   * Informs the RoboRIO to emit a signal which manually sets the controller's output.
   *
   * @param power Speed at which to run the motor (must be between 1.0 and -1.0)
   * @return a ControllerEvent setting the power of the controller
   */
  public static ControllerEvent power(double power) {
    return new RunAtPowerEvent(power);
  }

  /**
   * Changes the mode of the controller.
   *
   * @param mode mode in which to run the controller
   * @return a ControllerEvent setting the mode of the controller
   */
  public static ControllerEvent mode(ControlMode mode) {
    return new LoopModeEvent(mode);
  }

  /**
   * Set a controller to follow another controller.
   *
   * @param target port of controller to follow
   * @return a ControllerEvent setting the leader of the controller
   */
  public static ControllerEvent follow(LoopSpeedController target) {
    return new FollowEvent(target);
  }

  public static ControllerEvent setOutputVoltage(double min, double max) {
    return new OutputVoltageEvent(min, max);
  }

  public static ControllerEvent resetPosition(double position) {
    return new ResetPositionEvent(position);
  }
}
