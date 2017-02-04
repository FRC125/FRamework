package com.nutrons.framework.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.nutrons.framework.controllers.CanControlMode;
import com.nutrons.framework.controllers.CanControllerProxy;
import com.nutrons.framework.controllers.ControllerEvent;
import com.nutrons.framework.controllers.ControllerMode;
import com.nutrons.framework.controllers.LoopModeEvent;
import com.nutrons.framework.controllers.RunAtPowerEvent;
import com.nutrons.framework.controllers.Talon;
import org.junit.Test;

/**
 * Created by krzw9 on 1/31/2017.
 */
public class TestMotorEvents {

  @Test
  public void testPowerEvent() {
    CanControllerProxy realController = mock(CanControllerProxy.class);
    Talon talon = new Talon(realController);
    ControllerEvent runEvent = new RunAtPowerEvent(0.5);
    talon.accept(runEvent);

    verify(realController).set(0.5);
  }

  @Test
  public void testSpeedEvent() {
    CanControllerProxy realController = mock(CanControllerProxy.class);
    Talon talon = new Talon(realController);
    ControllerEvent speedEvent = new LoopModeEvent(ControllerMode.LOOP_SPEED);
    speedEvent.actOn(talon);

    verify(realController).changeControlMode(CanControlMode.Speed);
  }
}
