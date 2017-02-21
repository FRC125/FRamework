package com.nutrons.framework.controllers;

import edu.wpi.first.wpilibj.Preferences;


public class TuneablePID {

  private final String label;
  private final double tuneableP;
  private final double tuneableI;
  private final double tuneableD;
  private final double tuneableF;

  public TuneablePID(String label, double tuneableP, double tuneableI, double tuneableD, double tuneableF) {
    this.label = label;
    this.tuneableP = tuneableP;
    this.tuneableI = tuneableI;
    this.tuneableD = tuneableD;
    this.tuneableF = tuneableF;
  }

  public void getPID() {
    Preferences.getInstance().getDouble(label, tuneableP);
    Preferences.getInstance().getDouble(label, tuneableI);
    Preferences.getInstance().getDouble(label, tuneableD);
    Preferences.getInstance().getDouble(label, tuneableP);
 }
}
