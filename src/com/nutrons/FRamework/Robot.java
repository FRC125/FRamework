package com.nutrons.FRamework;

import com.nutrons.FRamework.util.CompMode;
import com.nutrons.FRamework.util.FlowOperators;
import edu.wpi.first.wpilibj.SampleRobot;
import io.reactivex.Flowable;
import static com.nutrons.FRamework.util.CompMode.*;

public abstract class Robot extends SampleRobot {

  private StreamManager sm;

  /**
   * Bootstraps a StreamManager with appropriate factories.
   * Subclasses should setup factories in this constructor.
   */
  public Robot() {

  }

  /**
   * Initializes the StreamManager.
   */
  @Override
  protected final void robotInit() {
    this.sm = this.createStreamManager();
  }

  protected abstract StreamManager createStreamManager();

  /**
   * A Flowable of booleans representing changes in enabled state over time.
   * Will emit item only when state changes.
   */
  protected final Flowable<Boolean> enabledStream() {
    return FlowOperators.toFlow(this::isEnabled).distinctUntilChanged();
  }

  /**
   * A Flowable of competition modes over time, representing a change
   * in the competition mode to test, auto or teleop.
   * Emits an item only when mode changes.
   */
  protected final Flowable<CompMode> competitionStream() {
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
    this.sm.startCompetition();
  }

  @Override
  public final void startCompetition() {
    super.startCompetition();
  }

  @Override
  protected final void disabled() {

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