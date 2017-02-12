package com.nutrons.framework.inputs;

import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper around WPI's "Joysticks" which provides Flowables for Gamepad data.
 */
public class WpiGamepad {

  private final int port;
  private final Joystick joystick;
  private final Map<Integer, Flowable<Boolean>> buttons;
  private final Map<Integer, Flowable<Double>> axes;

  /**
   * Create Gamepad streams from a WPI "Joystick."
   * axisMN represents the channel on which the Mth joystick's N-axis resides.
   */
  public WpiGamepad(int port) {
    this.port = port;
    this.joystick = new Joystick(this.port);
    this.axes = new HashMap<>();
    this.buttons = new HashMap<>();
  }

  private Flowable<Double> memoizedAxis(int axisNumber) {
    if (!axes.containsKey(axisNumber)) {
      synchronized (axes) {
        if (!axes.containsKey(axisNumber)) {
          axes.put(axisNumber,
              FlowOperators.toFlow(() ->
                  this.joystick.getRawAxis(axisNumber)));
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
      synchronized (buttons) {
        if (!buttons.containsKey(buttonNumber)) {
          buttons.put(buttonNumber,
              FlowOperators.toFlow(() ->
                  new JoystickButton(this.joystick, buttonNumber).get())
                  .distinctUntilChanged());
        }
      }
    }
    return buttons.get(buttonNumber);
  }

  /**
   * A Flowable representing the values of the specified button on the gamepad.
   */
  public Flowable<Boolean> button(int buttonNumber) {
    return memoizedButton(buttonNumber);
  }
}
