package com.nutrons.framework;

import static com.nutrons.framework.util.CompMode.AUTO;
import static com.nutrons.framework.util.CompMode.TELE;
import static com.nutrons.framework.util.CompMode.TEST;

import com.nutrons.framework.factories.InputManager;
import com.nutrons.framework.factories.OutputManager;
import com.nutrons.framework.factories.WpiFactory;
import com.nutrons.framework.subsystems.WpiSettings;
import com.nutrons.framework.util.CompMode;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.SampleRobot;
import io.reactivex.Flowable;

public abstract class Robot extends SampleRobot {

  private StreamManager sm;

  /**
   * Bootstraps a StreamManager with appropriate factories.
   */
  public Robot() {

  }

  /**
   * Initializes the StreamManager.
   */
  @Override
  protected final void robotInit() {
    this.sm = this.provideStreamManager();
  }

  /**
   * Creates an instance of StreamManager, and then
   * registers all of its subsystems before returning
   *
   * @return A StreamManager instance with all of its subsystems registered.
   */
  protected abstract StreamManager provideStreamManager();

  /**
   * Sets the factory instances for input and output managers.
   * Can be overridden by subclasses to use different factories.
   */
  protected void setupFactoryManagers() {
    WpiFactory wpi = new WpiFactory();
    InputManager.setFactory(wpi);
    OutputManager.setFactory(wpi);
    OutputManager.factory().setSettingsInstance(new WpiSettings());
  }

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