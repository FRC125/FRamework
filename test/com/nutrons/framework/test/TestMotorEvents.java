package com.nutrons.framework.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.nutrons.framework.controllers.CanControllerProxy;
import com.nutrons.framework.controllers.ControllerEvent;
import com.nutrons.framework.controllers.ControlMode;
import com.nutrons.framework.controllers.FollowEvent;
import com.nutrons.framework.controllers.LoopModeEvent;
import com.nutrons.framework.controllers.RunAtPowerEvent;
import com.nutrons.framework.controllers.Talon;
import org.junit.Before;
import org.junit.Test;

public class TestMotorEvents {

  private CanControllerProxy realController;
  private Talon talon;

  @Before
  public void initialize(){
    this.realController = mock(CanControllerProxy.class);
    this.talon = new Talon(realController);
  }

  @Test
  public void testPowerEvent() {
    ControllerEvent runEvent = new RunAtPowerEvent(0.5);
    talon.accept(runEvent);

    verify(realController).changeControlMode(ControlMode.MANUAL);
    verify(realController).set(0.5);
  }

  @Test
  public void testSpeedEvent() {
    ControllerEvent speedEvent = new LoopModeEvent(ControlMode.LOOP_SPEED);
    speedEvent.actOn(talon);

    verify(realController).changeControlMode(ControlMode.LOOP_SPEED);
  }

  @Test
  public void testFollowEvent(){
    ControllerEvent speedEvent = new FollowEvent(1);
    speedEvent.actOn(talon);

    verify(realController).changeControlMode(ControlMode.FOLLOWER);
    verify(realController).set(1);
  }
}
