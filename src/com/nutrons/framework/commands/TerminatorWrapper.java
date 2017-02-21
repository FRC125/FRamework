package com.nutrons.framework.commands;

/**
 * A wrapper which allows a 'dispose' runnable to be wrapped such that it is idempotent.
 */
public class TerminatorWrapper implements Terminator {

  private final Runnable terminate;
  private volatile boolean terminated;

  public TerminatorWrapper(Runnable terminate) {
    this.terminated = false;
    this.terminate = terminate;
  }

  @Override
  public void run() {
    if (!terminated) {
      synchronized (terminate) {
        if (!terminated) {
          this.terminated = true;
          terminate.run();
        }
      }
    }
  }
}
