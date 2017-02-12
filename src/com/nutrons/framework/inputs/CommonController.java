package com.nutrons.framework.inputs;

import io.reactivex.Flowable;

public class CommonController extends WpiGamepad {
  private int xButton;
  private int aButton;
  private int bButton;
  private int yButton;
  private int startButton;
  private int rightBumper;
  private int leftBumper;
  private int selectButton;

  private CommonController(int port,
                           int axis1X,
                           int axis1Y,
                           int axis2X,
                           int axis2Y,
                           int buttonX,
                           int buttonY,
                           int buttonB,
                           int buttonA,
                           int buttonStart,
                           int ButtonSelect,
                           int bumperRight,
                           int bumperleft) {
    super(port, axis1X, axis1Y, axis2X, axis2Y);
    this.xButton = buttonX;
    this.yButton = buttonY;
    this.bButton = buttonB;
    this.aButton = buttonA;
    this.startButton = buttonStart;
    this.selectButton = ButtonSelect;
    this.rightBumper = bumperRight;
    this.leftBumper = bumperleft;


  }

  public static CommonController logitech(int port) {
    return new CommonController(
        port,
        InputMap.LOGITECH_X_1,
        InputMap.LOGITECH_Y_1,
        InputMap.LOGITECH_X_2,
        InputMap.LOGITECH_Y_2,
        InputMap.LOGITECH_X,
        InputMap.LOGITECH_Y,
        InputMap.LOGITECH_B,
        InputMap.LOGITECH_A,
        InputMap.LOGITECH_START,
        InputMap.LOGITECH__SELECT,
        InputMap.LOGITECH_RIGHT_BUMPER,
        InputMap.LOGITECH_LEFT_BUMPER);
  }
  public static CommonController Xbox(int port){
    return new CommonController(port,
        InputMap.XBOX_X_1,
        InputMap.XBOX_Y_1,
        InputMap.XBOX_X_2,
        InputMap.XBOX_Y_2,
        InputMap.XBOX_X,
        InputMap.XBOX_Y,
        InputMap.XBOX_B,
        InputMap.XBOX_A,
        InputMap.XBOX_START,
        InputMap.XBOX_SELECT,
        InputMap.XBOX_RIGHT_BUMPER,
        InputMap.XBOX_LEFT_BUMPER);
  }


  public Flowable<Boolean> xButton() {
    return button(xButton);
  }

  public Flowable<Boolean> aButton() {
    return button(aButton);
  }

  public Flowable<Boolean> yButton() {
    return button(yButton);
  }

  public Flowable<Boolean> bButton() {
    return button(bButton);
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
  public Flowable<Boolean> leftBumper(){
    return button(leftBumper);
  }
}

