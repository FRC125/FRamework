package com.nutrons.FRamework.consumers;

public class SetLoopEvent implements MotorEvent {
  private final boolean setTo;

  public SetLoopEvent(boolean setTo) {
    this.setTo = setTo;
  }
  
  @Override
  public void actOn(LoopSpeedController controller) {
    if(this.setTo) {
      controller.loopEnable();
    } else {
      controller.loopDisable();
    }
  }
}
