package com.nutrons.framework.controllers;

public class TuneablePID {

  private final String label;
  private final Tuneable tuneableP;
  private final Tuneable tuneableI;
  private final Tuneable tuneableD;
  private final Tuneable tuneableF;
  
  public TuneablePID(String label, double defaultP, double defaultI, double defaultD, double defaultF) {
    this.label = label;
    this.tuneableP = new Tuneable(label + "_P", defaultP);
    this.tuneableI = new Tuneable(label + "_I", defaultI);
    this.tuneableD = new Tuneable(label + "_D", defaultD);
    this.tuneableF = new Tuneable(label + "_F", defaultF);
  }

  public TuneablePID(String label) {
    this(label, 0, 0, 0, 0);
  }

  public ControllerEvent getPID() {
    return Events.pid(tuneableP.get(), tuneableI.get(), tuneableD.get(),
        tuneableF.get());
  }
}
