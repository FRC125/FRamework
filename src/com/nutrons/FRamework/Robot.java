package com.nutrons.FRamework;

import static com.nutrons.FRamework.CompMode.*;

import edu.wpi.first.wpilibj.SampleRobot;
import io.reactivex.Flowable;

public abstract class Robot extends SampleRobot {

  private StreamManager sm;

  /**
   * Bootstraps the StreamManager with appropriate factories.
   */
  public Robot() {

  }


  /**
   * Initializes the StreamManager
   */
  @Override
  protected final void robotInit() {
    this.sm = this.createStreamManager();
  }

  /**
   * Construct the StreamManager for the physical robot
   */
  protected abstract StreamManager createStreamManager();

  protected final Flowable<Boolean> enabledStream() {
    return Util.changedValues(Util.toFlow(this::isEnabled));
  }

  protected final Flowable<CompMode> competitionStream() {
    return Util.changedValues(Flowable.merge(
        Util.toFlow(this::isAutonomous).filter(x -> x).map((x) -> AUTO),
        Util.toFlow(this::isOperatorControl).filter(x -> x).map((x) -> TELEOP),
        Util.toFlow(this::isTest).filter(x -> x).map((x) -> TEST)));
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