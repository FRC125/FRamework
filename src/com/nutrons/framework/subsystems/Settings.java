package com.nutrons.framework.subsystems;

import com.nutrons.framework.Subsystem;

public interface Settings extends Subsystem {

  /**
   * Get int from settings table.
   */
  int getInt(String key);

  /**
   * Get double from settings table.
   */
  double getDouble(String key);
}
