package com.nutrons.FRamework;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class sets up the I/O factories and initializes subsystems.
 */
public class StreamManager {
  private List<Subsystem> subsystems;
  private Flowable<Boolean> enabled;
  private Flowable<CompMode> mode;

  /**
   * Manages subsystems and the competition loop.
   * @param enabled a Flowable of booleans, representing changes in the enabled state of the robot
   * @param mode a Flowable of CompModes, representing changes in the competition game mode
   */
  public StreamManager(Flowable<Boolean> enabled, Flowable<CompMode> mode) {
    this.subsystems = new ArrayList<>();
    this.enabled = enabled;
    this.mode = mode;
  }

  /**
   * Called to subscribe subsystem streams, and start the competition loop.
   */
  public void startCompetition() {
    Observable.fromIterable(this.subsystems).blockingSubscribe(Subsystem::registerSubscriptions);
    this.enabled.ignoreElements().blockingAwait();
    this.mode.ignoreElements().blockingAwait();
  }

  /**
   * @return A Flowable representing the "enabled" state of the robot over time.
   */
  public final Flowable<Boolean> enabled() {
    return this.enabled;
  }

  /**
   * @return A Flowable representing the "competition mode" of the robot over time.
   */
  public final Flowable<CompMode> mode() {
    return this.mode;
  }

  /**
   * Register a subsystem, so it will only subscribe side effect consumers
   * (such as motors) once the Robot is initialized.
   */
  protected final void registerSubsystem(Subsystem subsystem) {
    this.subsystems.add(subsystem);
  }
}
