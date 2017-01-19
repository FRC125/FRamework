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
  public void onNext(MotorEvent motorEvent) {
    motorEvent.actOn(this);
  }

  @Override
  void setRunAtPower(double power) {
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
}
