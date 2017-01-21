package com.nutrons.FRamework.subsystems;

import com.nutrons.FRamework.Subsystem;
import com.nutrons.FRamework.util.FlowOperators;
import edu.wpi.first.wpilibj.Preferences;
import java.util.concurrent.TimeUnit;

public class Settings implements Subsystem {
  private volatile Preferences preferences;
  private static Settings instance;

  private Settings() {

  }

  /**
   * Get the Settings instance, it is a singleton.
   */
  public static synchronized Settings instance() {
    if (Settings.instance == null) {
      Settings.instance = new Settings();
    }
    return Settings.instance;
  }

  @Override
  public void registerSubscriptions() {
    FlowOperators.toFlow(() -> this.preferences = Preferences.getInstance(),
        500, TimeUnit.MILLISECONDS);
  }

  /**
   * Get int from settings table.
   */
  public int getInt(String key) {
    return this.preferences.getInt(key, 0);
  }

  /**
   * Get double from settings table.
   */
  public double getDouble(String key) {
    return this.preferences.getDouble(key, 0.0);
  }
}
