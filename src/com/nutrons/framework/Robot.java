package com.nutrons.framework;

import static com.nutrons.framework.util.CompMode.AUTO;
import static com.nutrons.framework.util.CompMode.TELE;
import static com.nutrons.framework.util.CompMode.TEST;

import com.nutrons.framework.commands.Command;
import com.nutrons.framework.util.CompMode;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.SampleRobot;
import io.reactivex.Flowable;

public abstract class Robot extends SampleRobot {

  private StreamManager sm;

  /**
   * Bootstraps a StreamManager.
   */
  public Robot() {

  }

  /**
   * Initializes the StreamManager.
   */
  @Override
  protected final void robotInit() {
    constructStreams();
    sm = this.provideStreamManager();
  }


  /**
   * Construct input and output streams that will be passed
   * as dependencies for creating subsystems.
   */
  protected abstract void constructStreams();

  /**
   * Creates an instance of each subsystem and a StreamManager, and then
   * registers all of StreamManager's subsystems before returning
   *
   * @return A StreamManager instance with all of its subsystems registered.
   */
  protected abstract StreamManager provideStreamManager();

  /**
   * A Flowable of booleans representing changes in enabled state over time.
   * Will emit item only when state changes.
   */
  public final Flowable<Boolean> enabledStream() {
    return FlowOperators.toFlow(this::isEnabled).distinctUntilChanged();
  }

  /**
   * A Flowable of competition modes over time, representing a change
   * in the competition mode to test, auto or teleop.
   * Emits an item only when mode changes.
   */
  public final Flowable<CompMode> competitionStream() {
    return Flowable.merge(
        FlowOperators.toFlow(this::isAutonomous).filter(x -> x).map((x) -> AUTO),
        FlowOperators.toFlow(this::isOperatorControl).filter(x -> x).map((x) -> TELE),
        FlowOperators.toFlow(this::isTest).filter(x -> x).map((x) -> TEST)).distinctUntilChanged();
    // filter(x -> x) will filter all false values from the stream.
  }

  /**
   * Called at start of competition.
   */
  @Override
  public final void robotMain() {
    Command auto = this.registerAuto();
    if (auto != null) {
      this.competitionStream().filter(x -> x == AUTO)
          .subscribe(x -> auto.terminable(competitionStream().filter(y -> y != AUTO)).execute());
    }
    System.out.println("registering subsystems");
    this.sm.startCompetition();
  }

  @Override
  public final void startCompetition() {
    super.startCompetition();
  }

  @Override
  protected final void disabled() {
  }

  protected Command registerAuto() {
    return null;
  }

  @Override
  public final void autonomous() {
  }

  @Override
  public final void operatorControl() {
  }

  @Override
  public final void test() {
  }

  @Override
  public final void free() {
    super.free();
  }

  @Override
  public final boolean isDisabled() {
    return super.isDisabled();
  }

  @Override
  public final boolean isEnabled() {
    return super.isEnabled();
  }

  @Override
  public final boolean isAutonomous() {
    return super.isAutonomous();
  }

  @Override
  public final boolean isTest() {
    return super.isTest();
  }

  @Override
  public final boolean isOperatorControl() {
    return super.isOperatorControl();
  }

  @Override
  public final boolean isNewDataAvailable() {
    return super.isNewDataAvailable();
  }
}