package com.nutrons.FRamework.consumers;

public class LoopStateEvent implements LoopControllerEvent {
  private final boolean setTo;

  public LoopStateEvent(boolean setTo) {
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
