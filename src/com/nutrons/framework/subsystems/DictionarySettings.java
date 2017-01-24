package com.nutrons.framework.subsystems;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a settings table which only provides the entries given by the constructor.
 */
public class DictionarySettings implements Settings {

  private final HashMap<String, Integer> ints;
  private final HashMap<String, Double> doubles;

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
    return ints.containsKey(key) ? ints.get(key) : 0;
  }

  @Override
  public double getDouble(String key) {
    return doubles.containsKey(key) ? doubles.get(key) : 0.0;
  }

  @Override
  public void registerSubscriptions() {

  }
}
