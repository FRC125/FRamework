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
import static com.nutrons.framework.util.CompMode.TELE;
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
   * @param mode    a Flowable of CompModes, representing changes in the competition game mode
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
  public final void startCompetition(Supplier<Command> autoSupplier, Supplier<Command> teleopSupplier) {
    Observable.fromIterable(this.subsystems).blockingSubscribe(x -> {
      System.out.println("registering " + x.getClass().getName());
      x.registerSubscriptions();
      System.out.println("registered " + x.getClass().getName());
    });
    System.out.println("all subsystems registered");
    Command auto = autoSupplier.get();
    if (auto != null) {
      combineLatest(this.enabled, this.mode, (x, y) -> x && y.compareTo(AUTO) == 0)
          .subscribeOn(Schedulers.io())
          .filter(x -> x)
          .map((a) -> {
            System.out.println("Starting Autonomous: " + auto.getClass().toString());
            return a;
          }).subscribe(x -> auto.terminable(mode.filter(y -> y != AUTO)
          .mergeWith(enabled.filter(z -> z).map(z -> AUTO))).startExecution());
    }
    Command tele = teleopSupplier.get();
    if (tele != null) {
      combineLatest(this.enabled, this.mode, (x, y) -> x && y.compareTo(TELE) == 0)
          .subscribeOn(Schedulers.io())
          .filter(x -> x)
          .map((a) -> {
            System.out.println("Starting Teleop: " + tele.getClass().toString());
            return a;
          }).subscribe(x -> tele.terminable(mode.filter(y -> y != TELE)
          .mergeWith(enabled.filter(z -> z).map(z -> TELE))).startExecution());
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
