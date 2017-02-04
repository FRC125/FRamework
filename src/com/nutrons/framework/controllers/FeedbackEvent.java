package com.nutrons.framework.controllers;

/**
 * Represents feedback from a LoopSpeedController's encoder.
 */
public interface FeedbackEvent {
  double error();
}
