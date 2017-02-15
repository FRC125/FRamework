package com.nutrons.framework.inputs;

import com.nutrons.framework.Subsystem;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A wrapper around WPI's "Joysticks" which provides Flowables for Gamepad data.
 */
public class WpiGamepad implements Subsystem {

  private final int port;
  private final Joystick joystick;

  private final Map<Integer, ConnectableFlowable<Boolean>> buttons;
  private final Map<Integer, JoystickButton> joyButtons;

  private final Map<Integer, ConnectableFlowable<Double>> axes;
  private final AtomicBoolean lock;

  /**
   * Create Gamepad streams from a WPI "Joystick."
   * axisMN represents the channel on which the Mth joystick's N-axis resides.
   */
  public WpiGamepad(int port) {
    this.lock = new AtomicBoolean(false);
    this.port = port;
    this.joystick = new Joystick(this.port);
    this.axes = new HashMap<>();
    this.buttons = new HashMap<>();
    this.joyButtons = new HashMap<>();
  }

  private Flowable<Double> memoizedAxis(int axisNumber) {
    if (!axes.containsKey(axisNumber)) {
      synchronized (lock) {
        if (!axes.containsKey(axisNumber)) {
          axes.put(axisNumber,
              FlowOperators.toFlow(() ->
                  this.joystick.getRawAxis(axisNumber)).publish());
        }
        if (lock.get()) {
          buttons.get(axisNumber).connect();
        }
      }
    }
    return axes.get(axisNumber);
  }

  /**
   * A Flowable representing the sensor values of the given axes on this gamepad.
   */
  public Flowable<Double> axis(int axisNumber) {
    return memoizedAxis(axisNumber);
  }

  private Flowable<Boolean> memoizedButton(int buttonNumber) {
    if (!buttons.containsKey(buttonNumber)) {
      synchronized (lock) {
        if (!buttons.containsKey(buttonNumber)) {
          buttons.put(buttonNumber,
              FlowOperators.toFlow(() ->
                  getJoyButton(buttonNumber).get())
                  .distinctUntilChanged().publish());
        }
        if (lock.get()) {
          buttons.get(buttonNumber).connect();
        }
      }
    }
    return buttons.get(buttonNumber);
  }

  private JoystickButton getJoyButton(int buttonNumber) {
    if (!joyButtons.containsKey(buttonNumber)) {
      synchronized (joyButtons) {
        if (!joyButtons.containsKey(buttonNumber)) {
          joyButtons.put(buttonNumber, new JoystickButton(this.joystick, buttonNumber));
        }
      }
    }
    return joyButtons.get(buttonNumber);
  }

  public boolean joyButton(int buttonNumber) {
    return getJoyButton(buttonNumber).get();
  }

  /**
   * A Flowable representing the values of the specified button on the gamepad.
   */
  public Flowable<Boolean> button(int buttonNumber) {
    return memoizedButton(buttonNumber);
  }

  @Override
  public void registerSubscriptions() {
    synchronized (lock) {
      lock.set(true);
      Flowable.<ConnectableFlowable>fromIterable(axes.values())
          .mergeWith(Flowable.<ConnectableFlowable>fromIterable(buttons.values()))
          .blockingSubscribe(ConnectableFlowable::connect);
    }
  }
}
