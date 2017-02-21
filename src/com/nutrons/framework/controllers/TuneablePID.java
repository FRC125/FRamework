package com.nutrons.framework.controllers;

public class TuneablePID {

  private final String label;
  private final Tuneable tuneableP;
  private final Tuneable tuneableI;
  private final Tuneable tuneableD;
  private final Tuneable tuneableF;

  public TuneablePID(String label, Tuneable tuneableP, Tuneable tuneableI, Tuneable tuneableD, Tuneable tuneableF) {
    this.label = label;
    this.tuneableP = tuneableP;
    this.tuneableI = tuneableI;
    this.tuneableD = tuneableD;
    this.tuneableF = tuneableF;
  }

  public LoopPropertiesEvent getPID() {
    return new LoopPropertiesEvent(tuneableP.get(), tuneableI.get(), tuneableD.get(), tuneableF.get());
 }
}
