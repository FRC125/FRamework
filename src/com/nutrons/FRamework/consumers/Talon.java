package com.nutrons.FRamework.consumers;

import com.ctre.CANTalon;

public class Talon extends LoopSpeedController {
  private final CANTalon.TalonControlMode loopMode;
  private CANTalon talon;

  public Talon(CANTalon talon, CANTalon.TalonControlMode loopMode) {
    this.talon = talon;
    this.loopMode = loopMode;
  }

  @Override
  public void onNext(LoopControllerEvent motorEvent) {
    motorEvent.actOn(this);
  }

  @Override
  void runAtPower(double power) {
    this.loopDisable();
    this.talon.set(power);
  }

  @Override
  void loopEnable() {
    this.talon.changeControlMode(this.loopMode);
  }

  @Override
  void loopDisable() {
    this.talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
  }

  @Override
  void setLoopProperties(double p, double i, double d, double f) {
    this.talon.setPID(p, i, d);
    this.talon.setF(f);
  }
}
