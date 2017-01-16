package com.nutrons.FRamework;

import io.reactivex.Flowable;

/**
 * Ultimately, this will be a main class which will enable creating
 * a StreamManager which can use virtualized inputs and outputs.
 */
public class Simulation {

  private StreamManager sm;

  private Simulation(Flowable<Boolean> enabled, Flowable<CompMode> mode) {
    this.sm = new StreamManager(enabled, mode);
  }

  protected final void startCompetition() {
    this.sm.startCompetition();
  }

  public static void main(String[] args) {
    Simulation simulation = new Simulation(Flowable.never(), Flowable.never());
    simulation.startCompetition();
  }
}
