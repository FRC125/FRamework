package com.nutrons.framework.controllers;

import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.processors.PublishProcessor;

public class VirtualSpeedController extends LoopSpeedController {

  private final PublishProcessor<Boolean> outputDirection;
  private final ConnectableFlowable<Boolean> outputDirectionReplay;
  private final PublishProcessor<Double[]> pidProperties;
  private final ConnectableFlowable<Double[]> pidPropertiesReplay;
  private final Flowable<Double> position;
  private final Flowable<Double> speed;
  private final PublishProcessor<Double> setpoint;
  private final ConnectableFlowable<Double> setpointReplay;
  private final PublishProcessor<Double> rawOutput;
  private final ConnectableFlowable<Double> rawOutputReplay;
  private final PublishProcessor<ControlMode> mode;
  private final ConnectableFlowable<ControlMode> modeReplay;
  private final PublishProcessor<Boolean> sensorDirection;
  private final ConnectableFlowable<Boolean> sensorDirectionRelay;
  private volatile double lastSpeed;
  private volatile double lastPosition;

  public VirtualSpeedController(Flowable<Double> position, Flowable<Double> speed) {
    this.outputDirection = PublishProcessor.create();
    this.outputDirectionReplay = this.outputDirection.distinctUntilChanged().replay(1);
    this.pidProperties = PublishProcessor.create();
    this.pidPropertiesReplay = this.pidProperties.distinctUntilChanged().replay(1);
    this.setpoint = PublishProcessor.create();
    this.setpointReplay = this.setpoint.distinctUntilChanged().replay(1);
    this.rawOutput = PublishProcessor.create();
    this.rawOutputReplay = this.rawOutput.distinctUntilChanged().replay(1);
    this.mode = PublishProcessor.create();
    this.modeReplay = this.mode.distinctUntilChanged().replay(1);
    this.sensorDirection = PublishProcessor.create();
    this.sensorDirectionRelay = this.sensorDirection.distinctUntilChanged().replay(1);

    this.modeReplay.connect();
    this.rawOutputReplay.connect();
    this.outputDirectionReplay.connect();
    this.pidPropertiesReplay.connect();
    this.setpointReplay.connect();

    this.position = position;
    this.speed = speed;
    this.position.subscribe(x -> this.lastPosition = x);
    this.speed.subscribe(x -> this.lastSpeed = x);
  }

  public VirtualSpeedController() {
    this(Flowable.just(0.0), Flowable.just(0.0));
  }

  @Override
  public double getCurrent() {
    return 0;
  }

  @Override
  public Flowable<FeedbackEvent> feedback() {
    return Flowable.empty();
  }

  @Override
  public void accept(ControllerEvent controllerEvent) {
    controllerEvent.actOn(this);
  }

  public Flowable<Boolean> outputDirection() {
    return outputDirectionReplay;
  }

  @Override
  public void setOutputFlipped(boolean flipped) {
    outputDirection.onNext(flipped);
  }

  @Override
  public boolean fwdLimitSwitchClosed() {
    return false;
  }

  @Override
  public boolean revLimitSwitchClosed() {
    return false;
  }

  @Override
  public double position() {
    return lastPosition;
  }

  @Override
  public void setVoltageRampRate(double v) {
    
  }

  public double speed() {
    return lastSpeed;
  }

  void changeControlMode(ControlMode mode) {
    this.mode.onNext(mode);
  }

  public Flowable<ControlMode> mode() {
    return modeReplay;
  }

  void setRawOutput(double output) {
    this.rawOutput.onNext(output);
  }

  public Flowable<Double> rawOutput() {
    return rawOutputReplay;
  }

  void setLoopProperties(double p, double i, double d, double f) {
    pidProperties.onNext(new Double[] {p, i, d, f});
  }

  public Flowable<Double[]> loopProperties() {
    return pidPropertiesReplay;
  }

  void changeSetpoint(double setpoint) {
    this.setpoint.onNext(setpoint);
  }

  public Flowable<Double> setpoint() {
    return setpointReplay;
  }

  public void reverseSensor(boolean b) {
    this.sensorDirection.onNext(b);
  }

  public Flowable<Boolean> sensorDirection() {
    return this.sensorDirectionRelay;
  }

  public void resetPositionTo(double position) {
    
  }
}
