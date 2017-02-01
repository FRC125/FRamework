package com.nutrons.framework.inputs;

import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import io.reactivex.Flowable;

public class WpiButton {
  JoystickButton button;
  int buttonNumber;
  Flowable<Boolean> values;

  public WpiButton(WpiGamepad wpiGamepad, int buttonNumber) {
    this.buttonNumber = buttonNumber;
    this.button = new JoystickButton(wpiGamepad.getJoystick(), buttonNumber);
    this.values = FlowOperators.toFlow(() -> this.button.get());
  }

  public Flowable<Boolean> values() {
      return this.values;
  }
}
