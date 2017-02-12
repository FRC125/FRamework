package com.nutrons.framework.test;

import com.nutrons.framework.controllers.*;
import org.junit.Test;

public class TestMotorEvents {

  @Test(expected = EventUnimplementedException.class)
  public void testUnimplemented() {
    Events.setpoint(1).actOn(new VirtualSpeedController());
  }

  @Test
  public void testImplemented() {
    ControllerEvent ce = new ControllerEvent() {
      @Override
      public void actOn(VirtualSpeedController controller) {

      }
    };
    ce.actOn(new VirtualSpeedController());
  }
}
