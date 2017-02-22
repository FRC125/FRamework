package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.Preferences;

public class Tuneable {

  private final String label;
  private final double defaultValue;

  public Tuneable(String label, double defaultValue) {
    this.label = label;
    this.defaultValue = defaultValue;
    Preferences.getInstance().putDouble(label, defaultValue);
  }

  public double get() {
    return Preferences.getInstance().getDouble(label, defaultValue);
  }
}