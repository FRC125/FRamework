package com.nutrons.framework.subsystems;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a settings table which only provides the entries given by the constructor.
 */
public class DictionarySettings implements Settings {

  private final HashMap<String, Integer> ints;
  private final HashMap<String, Double> doubles;

  /**
   * Constructs a settings table with the initial values provided in the maps.
   */
  public DictionarySettings(Map<String, Integer> ints, Map<String, Double> doubles) {
    this.ints = new HashMap<>();
    this.doubles = new HashMap<>();
    this.ints.putAll(ints);
    this.doubles.putAll(doubles);
  }

  public DictionarySettings() {
    this(new HashMap<>(), new HashMap<>());
  }

  @Override
  public int getInt(String key) {
    return ints.getOrDefault(key, 0);
  }

  @Override
  public double getDouble(String key) {
    return doubles.getOrDefault(key, 0.0);
  }

  public void registerSubscriptions() {

  }
}
