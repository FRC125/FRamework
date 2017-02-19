package com.nutrons.framework.controllers;

public class FollowEvent implements ControllerEvent {

  private final Talon target;

  /**
   * Creates a Follower event.
   * @param targetToFollow is a event of LoopSpeedController.
   */
  public FollowEvent(LoopSpeedController targetToFollow) {
    if (targetToFollow instanceof Talon) {
      this.target = (Talon) targetToFollow;
    } else {
      throw new EventUnimplementedException("Cannot follow this target!");
    }
  }

  @Override
  public void actOn(Talon talon) {
    talon.changeControlMode(ControlMode.FOLLOWER);
    talon.set(target.id());
  }
}
