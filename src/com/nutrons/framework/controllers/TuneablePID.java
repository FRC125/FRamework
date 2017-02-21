package com.nutrons.framework.controllers;

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

  public LoopPropertiesEvent getPID() {
    return new LoopPropertiesEvent(tuneableP, tuneableI, tuneableD, tuneableF);
 }
}
