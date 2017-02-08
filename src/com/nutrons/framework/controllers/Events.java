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
    return new LoopPropertiesEvent(setPoint, pval, ival, dval, fval);
  }

  /**
   * Set the power of a motor
   *
   * @param power Speed at which to run the motor (must be between 1.0 and -1.0)
   * @return a ControllerEvent setting the power of the controller
   */
  public static ControllerEvent power(double power) {
    return new RunAtPowerEvent(power);
  }

  /**
   * Change the mode of the controller.
   *
   * @param mode mode in which to run the controller
   * @return a ControllerEvent setting the mode of the controller
   */
  public static ControllerEvent mode(ControllerMode mode) {
    return new LoopModeEvent(mode);
  }

  /**
   * Set a controller to follow another controller.
   *
   * @param target port of controller to follow
   * @return a ControllerEvent setting the leader of the controller
   */
  public static ControllerEvent follow(int target) {
    return new FollowEvent(target);
  }

}
