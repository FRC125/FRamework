package com.nutrons.framework.controllers.pneumatics;

public class StartCompressorEvent implements PneumaticEvent {

  public StartCompressorEvent() {

  }

  public void actOn(WpiCompressor comp) {
    comp.start();
  }
}
