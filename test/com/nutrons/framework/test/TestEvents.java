package com.nutrons.framework.test;

import com.nutrons.framework.consumers.ControllerEvent;
import com.nutrons.framework.consumers.EventUnimplementedException;
import com.nutrons.framework.consumers.LoopSpeedController;
import org.junit.Test;

public class TestEvents {

  @Test(expected = EventUnimplementedException.class)
  public void testUnimplemented() {
    new ControllerEvent() {
    }.actOn(new SomeController(1));
  }

  class SomeController extends LoopSpeedController {

    public SomeController(int port) {
      super(port);
    }
  }

  @Test
  public void testImplemented() {
    new ControllerEvent() {
      public void actOn(SomeController controller) {

      }
    }.actOn(new SomeController(1));
  }
}