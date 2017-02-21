package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.Preferences;

public class Tuneable {
  private final String label;
  private final double defaultValue;

  public Tuneable(String label, double defaultValue) {
    this.label = label;
    this.defaultValue = defaultValue;
  }
  public double get(){
    Preferences.getInstance().getDouble(label, defaultValue);
    return defaultValue;
  }
  public void tunePID(double P, double I, double D, double F){
    Preferences.getInstance().getDouble("Tuneable P", P);
    Preferences.getInstance().getDouble("Tuneable I", I);
    Preferences.getInstance().getDouble("Tuneable D", D);
    Preferences.getInstance().getDouble("Tuneable F", F);
    return;
  }
  public void tuneSetpoint(double Setpoint){
    Preferences.getInstance().getDouble("Tuneable Setpoint", Setpoint);
    return;
  }
}