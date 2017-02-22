package com.nutrons.framework.controllers;

public class TuneablePID {

  private final String label;
  private final Tuneable tuneableP;
  private final Tuneable tuneableI;
  private final Tuneable tuneableD;
  private final Tuneable tuneableF;


  public TuneablePID(String label) {
    this.label = label;
    this.tuneableP = new Tuneable(label + "P", 0);
    this.tuneableI = new Tuneable(label + "I", 0);
    this.tuneableD = new Tuneable(label + "D", 0);
    this.tuneableF = new Tuneable(label + "F", 0);
  }

  public LoopPropertiesEvent getPID() {
    return new LoopPropertiesEvent(tuneableP.get(), tuneableI.get(), tuneableD.get(), tuneableF.get());
  }
}
