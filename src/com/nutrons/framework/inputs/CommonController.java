package com.nutrons.framework.inputs;

import io.reactivex.Flowable;

public class CommonController extends WpiGamepad {

  private final int leftStickX;
  private final int rightStickX;
  private final int leftStickY;
  private final int rightStickY;
  private final int leftTrigger;
  private final int rightTrigger;
  private final int buttonX;
  private final int buttonA;
  private final int buttonB;
  private final int buttonY;
  private final int startButton;
  private final int rightBumper;
  private final int leftBumper;
  private final int selectButton;
  private final int stickButtonLeft;
  private final int stickButtonRight;

  private CommonController(int port,
      int leftStickX,
      int leftStickY,
      int rightStickX,
      int rightStickY,
      int leftTrigger,
      int rightTrigger,
      int buttonX,
      int buttonY,
      int buttonB,
      int buttonA,
      int buttonStart,
      int buttonSelect,
      int rightBumper,
      int leftBumper,
      int stickButtonLeft,
      int stickButtonRight) {
    super(port);
    this.leftStickX = leftStickX;
    this.leftStickY = leftStickY;
    this.rightStickX = rightStickX;
    this.rightStickY = rightStickY;
    this.leftTrigger = leftTrigger;
    this.rightTrigger = rightTrigger;
    this.buttonX = buttonX;
    this.buttonY = buttonY;
    this.buttonB = buttonB;
    this.buttonA = buttonA;
    this.startButton = buttonStart;
    this.selectButton = buttonSelect;
    this.rightBumper = rightBumper;
    this.leftBumper = leftBumper;
    this.stickButtonLeft = stickButtonLeft;
    this.stickButtonRight = stickButtonRight;
  }

  /**
   * Create a CommonController with bindings for the Logitech F310 in D-mode.
   */
  public static CommonController logitechDf310(int port) {
    return new CommonController(
        port,
        InputMap.LOGITECH_X_1,
        InputMap.LOGITECH_Y_1,
        InputMap.LOGITECH_X_2,
        InputMap.LOGITECH_Y_2,
        InputMap.LOGITECH_LEFT_TRIGGER,
        InputMap.LOGITECH_RIGHT_TRIGGER,
        InputMap.LOGITECH_X,
        InputMap.LOGITECH_Y,
        InputMap.LOGITECH_B,
        InputMap.LOGITECH_A,
        InputMap.LOGITECH_START,
        InputMap.LOGITECH_SELECT,
        InputMap.LOGITECH_RIGHT_BUMPER,
        InputMap.LOGITECH_LEFT_BUMPER,
        InputMap.LOGITECH_L3,
        InputMap.LOGITECH_R3);
  }

  /**
   * Create a CommonController with bindings for the Xbox 360.
   */
  public static CommonController xbox360(int port) {
    return new CommonController(port,
        InputMap.XBOX_X_1,
        InputMap.XBOX_Y_1,
        InputMap.XBOX_X_2,
        InputMap.XBOX_Y_2,
        InputMap.XBOX_LEFT_TRIGGER,
        InputMap.XBOX_RIGHT_TRIGGER,
        InputMap.XBOX_X,
        InputMap.XBOX_Y,
        InputMap.XBOX_B,
        InputMap.XBOX_A,
        InputMap.XBOX_START,
        InputMap.XBOX_SELECT,
        InputMap.XBOX_RIGHT_BUMPER,
        InputMap.XBOX_LEFT_BUMPER,
        InputMap.XBOX_L3,
        InputMap.XBOX_R3);
  }

  public Flowable<Double> leftStickX() {
    return axis(leftStickX);
  }

  public Flowable<Double> leftStickY() {
    return axis(leftStickY);
  }

  public Flowable<Double> rightStickX() {
    return axis(rightStickX);
  }

  public Flowable<Double> rightStickY() {
    return axis(rightStickY);
  }

  public Flowable<Boolean> buttonX() {
    return button(buttonX);
  }

  public Flowable<Boolean> buttonA() {
    return button(buttonA);
  }

  public Flowable<Boolean> buttonY() {
    return button(buttonY);
  }

  public Flowable<Boolean> buttonB() {
    return button(buttonB);
  }

  public Flowable<Boolean> startButton() {
    return button(startButton);
  }

  public Flowable<Boolean> selectButton() {
    return button(selectButton);
  }

  public Flowable<Boolean> rightBumper() {
    return button(rightBumper);
  }

  public Flowable<Boolean> leftBumper() {
    return button(leftBumper);
  }

  public Flowable<Double> leftTrigger() {
    return axis(leftTrigger);
  }

  public Flowable<Double> rightTrigger() {
    return axis(rightTrigger);
  }

  public Flowable<Boolean> stickButtonLeft() {
    return button(stickButtonLeft);
  }

  public Flowable<Boolean> stickButtonRight() {
    return button(stickButtonRight);
  }
}

