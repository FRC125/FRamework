package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.Preferences;


public class TuneablePID {

  private final String label;
  private final double tuneablePID;

  public TuneablePID(String label, double tuneablePID) {
    this.label = label;
    this.tuneablePID = tuneablePID;
  }

  public void getPID(double P, double I, double D, double F) {
    Preferences.getInstance().getDouble(label, P);
    Preferences.getInstance().getDouble(label, I);
    Preferences.getInstance().getDouble(label, D);
    Preferences.getInstance().getDouble(label, F);
  }
}
