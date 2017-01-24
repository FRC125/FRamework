package com.nutrons.FRamework;

/**
 * Subsystems must only subscribe consumers which cause side effects
 * when their registerSubscriptions method is invoked.
 */
public interface Subsystem {

  /**
   * Subscribe consumers which perform side effect (such as motors) to their sources.
   * This is the ONLY instance where consumers which perform side effects should be subscribed.
   */
  void registerSubscriptions();
}
