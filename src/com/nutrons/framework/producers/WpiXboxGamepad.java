package com.nutrons.framework.producers;

/**
 * A WpiGamepad with default configuration for an Xbox360 controller.
 */
public class WpiXboxGamepad extends WpiGamepad {
  public WpiXboxGamepad(int port) {
    super(port, InputMap.XBOX_X_1, InputMap.XBOX_Y_1, InputMap.XBOX_X_2, InputMap.XBOX_Y_2);
  }
}
