package com.nutrons.framework.test;

import com.ctre.CANTalon;
import com.nutrons.framework.controllers.CanControllerProxy;
import com.nutrons.framework.controllers.ControllerEvent;
import com.nutrons.framework.controllers.RunAtPowerEvent;
import com.nutrons.framework.controllers.Talon;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by krzw9 on 1/31/2017.
 */
public class TestMotorEvents {

  @Test
  public void testTalonEvent(){
    CanControllerProxy realController = mock(CanControllerProxy.class);
    doNothing().when(realController).set(0.5);
    Talon talon = new Talon(realController);
    ControllerEvent runEvent = new RunAtPowerEvent(0.5);
    talon.accept(runEvent);

    verify(realController).set(0.5);
  }
}
