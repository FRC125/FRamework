package com.nutrons.framework.test;

import com.nutrons.framework.controllers.ControllerEvent;
import com.nutrons.framework.controllers.EventUnimplementedException;
import com.nutrons.framework.controllers.FeedbackEvent;
import com.nutrons.framework.controllers.LoopSpeedController;
import io.reactivex.Flowable;
import org.junit.Test;

public class TestEvents {

  @Test(expected = EventUnimplementedException.class)
  public void testUnimplemented() {
    new ControllerEvent() {
    }.actOn(new SomeController());
  }

  class SomeController extends LoopSpeedController {

    @Override
    public Flowable<FeedbackEvent> feedback() {
      return Flowable.never();
    }
  }

  @Test
  public void testImplemented() {
    new ControllerEvent() {
      public void actOn(SomeController controller) {

      }
    }.actOn(new SomeController());
  }
}
