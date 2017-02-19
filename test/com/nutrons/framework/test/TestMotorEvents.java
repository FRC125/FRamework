package com.nutrons.framework.test;

import com.nutrons.framework.controllers.ControllerEvent;
import com.nutrons.framework.controllers.EventUnimplementedException;
import com.nutrons.framework.controllers.Events;
import com.nutrons.framework.controllers.VirtualSpeedController;
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
