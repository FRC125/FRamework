package com.nutrons.framework.controllers;

import static com.ctre.CANTalon.FeedbackDevice;
import static com.ctre.CANTalon.SetValueMotionProfile;
import static com.ctre.CANTalon.TalonControlMode;
import static com.nutrons.framework.util.FlowOperators.toFlow;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Talon extends LoopSpeedController {

  private final Flowable<FeedbackEvent> feedback;
  private final CANTalon talon;
  private boolean reverseFeedback;
  private MotionWorker motionWorker = new MotionWorker();

  /**
   * Creates a talon on the given port.
   */
  public Talon(int port) {
    this(new CANTalon(port));
  }

  public Talon(CANTalon talon) {
    this.talon = talon;
    this.feedback = toFlow(() -> this.talon::getError);
  }

  public Talon(int port, FeedbackDevice feedbackDevice) {
    this(port);
    this.talon.setFeedbackDevice(feedbackDevice);
  }

  /**
   * Creates a talon that initially follows another talon.
   *
   * @param toFollow the talon to follow
   */
  public Talon(int port, Talon toFollow) {
    this(port);
    this.changeControlMode(ControlMode.FOLLOWER);
    this.set(toFollow.id());
  }

  void set(double value) {
    SmartDashboard.putNumber("talon " + this.id(), value);
    this.talon.set(value);
  }

  void changeControlMode(ControlMode mode) {
    switch (mode) {
      case FOLLOWER:
        this.talon.changeControlMode(TalonControlMode.Follower);
        break;
      case LOOP_POSITION:
        this.talon.changeControlMode(TalonControlMode.Position);
        break;
      case LOOP_SPEED:
        this.talon.changeControlMode(TalonControlMode.Speed);
        break;
      case MANUAL:
        this.talon.changeControlMode(TalonControlMode.PercentVbus);
        break;
      case MOTION_PROFILE:
        this.talon.changeControlMode(TalonControlMode.MotionProfile);
      default:
        throw new InvalidParameterException("This ControlMode is not supported!");
    }
  }

  void changeSetpoint(double setpoint) {
    this.talon.setSetpoint(setpoint);

  }

  public double getCurrent() {
    return this.talon.getOutputCurrent();
  }

  void setP(double pval) {
    this.talon.setP(pval);
  }

  void setI(double ival) {
    this.talon.setI(ival);
  }

  void setD(double dval) {
    this.talon.setD(dval);
  }

  void setF(double fval) {
    this.talon.setF(fval);
  }

  @Override
  public Flowable<FeedbackEvent> feedback() {
    return this.feedback;
  }

  @Override
  public void accept(ControllerEvent event) {
    event.actOn(this);
  }

  @Override
  public void setOutputFlipped(boolean flipped) {
    talon.setInverted(flipped);
  }

  @Override
  public double speed() {
    return this.talon.getSpeed();
  }

  public void setFeedbackDevice(FeedbackDevice device) {
    this.talon.setFeedbackDevice(device);
  }

  void reverseSensor(boolean flipped) {
    talon.reverseSensor(flipped);
  }

  int id() {
    return this.talon.getDeviceID();
  }

  void setOutputVoltage(double min, double max) {
    this.talon.configNominalOutputVoltage(Math.max(min, 0.0), Math.min(max, 0.0));
    this.talon.configPeakOutputVoltage(Math.max(max, 0.0), Math.min(min, 0.0));
  }

  @Override
  public double position() {
    return this.talon.getPosition();
  }

  void resetPositionTo(double position) {
    this.talon.setPosition(position);
  }


  @Override
  public boolean fwdLimitSwitchClosed() {
    return this.talon.isFwdLimitSwitchClosed();
  }

  @Override
  public boolean revLimitSwitchClosed() {
    return this.talon.isRevLimitSwitchClosed();
  }

  public double getClosedLoopError() {
    return this.talon.getClosedLoopError();
  }

  @Override
  public void setVoltageRampRate(double v) {
    this.talon.setVoltageRampRate(v);
  }

  @Override
  public void runMotionProfile(Flowable<Double[]> trajectoryPoints) {
    System.out.println("starting motion profile");
    this.talon.changeMotionControlFramePeriod(5);
    this.talon.set(SetValueMotionProfile.Disable.value);
    Completable buffer = this.motionWorker.startFilling(trajectoryPoints);
    System.out.println("filling buffers");
    this.motionWorker.resume();
    buffer.doOnComplete(() -> this.talon.set(SetValueMotionProfile.Enable.value)).blockingAwait();
    System.out.println("done filling buffers and enabled profile");
  }

  private class MotionWorker {
    private static final int MIN_TRAJECTORY_POINTS = 10;
    private final AtomicBoolean running;
    private boolean paused;
    private Thread worker;

    private MotionWorker() {
      this.running = new AtomicBoolean(false);
      this.paused = true;
      this.worker = new Thread(() -> {
        while (running.get()) {
          while (!paused) {
            try {
              Thread.sleep(5);
            } catch (InterruptedException e) {
            }
            Talon.this.talon.processMotionProfileBuffer();
          }
        }
      });
    }

    void pause() {
      this.paused = true;
    }

    void resume() {
      if (!this.running.get()) {
        synchronized (running) {
          if (!this.running.get()) {
            this.worker.start();
            this.running.set(true);
          }
        }
      }
      this.paused = false;
    }

    Completable startFilling(Flowable<Double[]> data) {
      Talon.this.talon.clearMotionProfileTrajectories();
      CANTalon.TrajectoryPoint point = new CANTalon.TrajectoryPoint();
      point.zeroPos = true;
      point.profileSlotSelect = 0;
      data = data.takeWhile(x -> this.running.get()).subscribeOn(Schedulers.io()).share();
      data.subscribe(x -> {
        synchronized (point) {
          point.position = x[0];
          point.velocity = x[1];
          point.timeDurMs = 10;
          Talon.this.talon.pushMotionProfileTrajectory(point);
          point.zeroPos = false;
        }
      });
      return data.take(MIN_TRAJECTORY_POINTS).ignoreElements();
    }
  }
}
