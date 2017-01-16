package com.nutrons.FRamework;

public interface Subsystem {
  /**
   * Subscribe side effect consumers (such as motors) to their sources.
   */
  void registerSubscriptions();
}
