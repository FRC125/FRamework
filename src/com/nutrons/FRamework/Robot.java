package com.nutrons.FRamework;

import static com.nutrons.FRamework.CompMode.AUTO;
import static com.nutrons.FRamework.CompMode.TELEOP;
import static com.nutrons.FRamework.CompMode.TEST;

import edu.wpi.first.wpilibj.RobotBase;
import io.reactivex.Flowable;

public class Robot extends RobotBase {

  private StreamManager sm;

  /**
   * Bootstraps the StreamManager with appropriate factories.
   */
  public Robot() {

  }

  /**
   * Called at start of competition.
   */
  @Override
  public final void startCompetition() {
    this.sm = new StreamManager(
        Util.changedValues(Util.toFlow(this::isEnabled)),
        Util.changedValues(Flowable.merge(
            Util.toFlow(this::isAutonomous).filter(x -> x).map((x) -> AUTO),
            Util.toFlow(this::isOperatorControl).filter(x -> x).map((x) -> TELEOP),
            Util.toFlow(this::isTest).filter(x -> x).map((x) -> TEST)))
    );
    this.sm.startCompetition();
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