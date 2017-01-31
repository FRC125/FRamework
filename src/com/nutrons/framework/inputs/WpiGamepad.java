package com.nutrons.framework.inputs;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import io.reactivex.Flowable;

/**
 * A wrapper around WPI's "Joysticks" which provides Flowables for Gamepad data.
 */
public class WpiGamepad {
  private final int port;
  private final Flowable<Double> axis1X;
  private final Flowable<Double> axis1Y;
  private final Flowable<Double> axis2X;
  private final Flowable<Double> axis2Y;
  private final Joystick joystick;

  /**
   * Create Gamepad streams from a WPI "Joystick."
   * axisMN represents the channel on which the Mth joystick's N-axis resides.
   */
  public WpiGamepad(int port, int axis1X, int axis1Y, int axis2X, int axis2Y) {
    this.port = port;
    this.joystick = new Joystick(this.port);
    this.axis1X = toFlow(() -> this.joystick.getRawAxis(axis1X));
    this.axis1Y = toFlow(() -> this.joystick.getRawAxis(axis1Y));
    this.axis2X = toFlow(() -> this.joystick.getRawAxis(axis2X));
    this.axis2Y = toFlow(() -> this.joystick.getRawAxis(axis2Y));
  }

  /**
   * A Flowable providing data from the first joystick's x-axis.
   **/
  public Flowable<Double> joy1X() {
    return this.axis1X;
  }

  /**
   * A Flowable providing data from the first joystick's y-axis.
   **/
  public Flowable<Double> joy1Y() {
    return this.axis1Y;
  }

  /**
   * A Flowable providing data from the second joystick's x-axis.
   **/
  public Flowable<Double> joy2X() {
    return this.axis2X;
  }

  /**
   * A Flowable providing data from the second joystick's y-axis.
   **/
  public Flowable<Double> joy2Y() {
    return this.axis2Y;
  }

  public class WpiButton{
    JoystickButton button;
    int buttonNumber;
    Flowable<Boolean> values;

    public WpiButton(WpiGamepad joystick, int buttonNumber){
      this.buttonNumber = buttonNumber;
      this.button = new JoystickButton(joystick.joystick, buttonNumber);
      this.values = FlowOperators.toFlow(() -> this.button.get());
    }

    public Flowable<Boolean> values(){
        return this.values;
    }
  }
}
