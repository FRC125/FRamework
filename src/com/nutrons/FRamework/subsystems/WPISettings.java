package com.nutrons.FRamework.subsystems;

import com.nutrons.FRamework.util.FlowOperators;
import edu.wpi.first.wpilibj.Preferences;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WPISettings implements Settings {
  private volatile Preferences prefs;
  private HashMap<String, Integer> ints;
  private HashMap<String, Double> doubles;

  public WPISettings() {
    this.ints = new HashMap<>();
    this.doubles = new HashMap<>();
  }

  @Override
  public void registerSubscriptions() {
    FlowOperators.toFlow(() -> this.prefs = Preferences.getInstance(),
        500, TimeUnit.MILLISECONDS);
  }


  @Override
  public int getInt(String key) {
    ints.put(key, prefs.getInt(key, ints.containsKey(key) ? ints.get(key) : 0));
    return ints.get(key);
  }

  @Override
  public double getDouble(String key) {
    doubles.put(key, prefs.getDouble(key, doubles.containsKey(key) ? doubles.get(key) : 0.0));
    return doubles.get(key);
  }
}
