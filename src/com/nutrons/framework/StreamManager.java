package com.nutrons.framework;

import com.nutrons.framework.commands.Command;
import com.nutrons.framework.util.CompMode;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.nutrons.framework.util.CompMode.AUTO;
import static io.reactivex.Flowable.combineLatest;

/**
 * This class sets up the I/O factories and initializes subsystems.
 */
public class StreamManager {

  private List<Subsystem> subsystems;
  private Flowable<Boolean> enabled;
  private Flowable<CompMode> mode;

  /**
   * Manages subsystems and the competition loop.
   * Subclasses should register subsystems in their constructor.
   *
   * @param robot the initial WPI Robot class
   */
  public StreamManager(Robot robot) {
    this(robot.enabledStream(), robot.competitionStream());
  }

  /**
   * Manages subsystems and the competition loop.
   * Subclasses should register subsystems in their constructor.
   *
   * @param enabled a Flowable of booleans, representing changes in the enabled state of the robot
   * @param mode a Flowable of CompModes, representing changes in the competition game mode
   */
  public StreamManager(Flowable<Boolean> enabled, Flowable<CompMode> mode) {
    this.subsystems = new ArrayList<>();
    this.enabled = enabled;
    this.mode = mode;
  }

  /**
   * Called to by the bootstrapper to subscribe subsystem streams,
   * and start the competition loop.
   */
  public final void startCompetition(Supplier<Command> autoSupplier) {
    Observable.fromIterable(this.subsystems).blockingSubscribe(Subsystem::registerSubscriptions);
    Command auto = autoSupplier.get();
    if (auto != null) {
      combineLatest(this.enabled, this.mode, (x, y) -> x && y.compareTo(AUTO) == 0)
              .subscribeOn(Schedulers.io())
              .filter(x -> x)
              .map((a) -> {
                System.out.println("Starting Autonomous" + auto.getClass().toString());
                return a;
              }).subscribe(x -> auto.terminable(mode.filter(y -> y != AUTO)
              .mergeWith(enabled.filter(z -> z).map(z -> AUTO))).execute());
    }
    this.enabled.ignoreElements().blockingAwait();
    this.mode.ignoreElements().blockingAwait();
    Observable.never().blockingSubscribe();
  }

  /**
   * Register a subsystem, so that during Robot setup,
   * the StreamManager will notify the subsystem that it should
   * subscribe its consumers (such as motors) to streams.
   */
  public final void registerSubsystem(Subsystem subsystem) {
    this.subsystems.add(subsystem);
  }
}
