package com.nutrons.framework.factories;

import com.nutrons.framework.consumers.ControllerEvent;
import com.nutrons.framework.subsystems.Settings;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Joystick;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.Map;

public class WpiFactory implements InputFactory, OutputFactory {

  private Map<Integer, Joystick> joysticks;
  private Settings settingsInstance;

  private Map<Integer, Consumer<ControllerEvent>> controllers;

  public WpiFactory() {
    this.joysticks = new HashMap<>();
    this.controllers = new HashMap<>();
  }

  private Flowable<Double> memoizedJoy(int port, int axis) {
    if (!this.joysticks.containsKey(port)) {
      Joystick joystick = new Joystick(port);
      this.joysticks.put(port,
          joystick);
    }
    return FlowOperators.toFlow(() -> this.joysticks.get(port).getRawAxis(axis));
  }

  @Override
  public Flowable<Double> controllerX1(int instance) {
    return memoizedJoy(instance, InputMap.XBOX_X_1);
  }

  @Override
  public Flowable<Double> controllerX2(int instance) {
    return memoizedJoy(instance, InputMap.XBOX_X_2);
  }

  @Override
  public Flowable<Double> controllerY1(int instance) {
    return memoizedJoy(instance, InputMap.XBOX_Y_1);
  }

  @Override
  public Flowable<Double> controllerY2(int instance) {
    return memoizedJoy(instance, InputMap.XBOX_Y_2);
  }

  /**
   * Sets the type of speed controller on each port.
   *
   * @param controllers Mapping from port number to controller
   */
  @Override
  public void setControllers(Map<Integer, Consumer<ControllerEvent>> controllers) {
    this.controllers.clear();
    this.controllers.putAll(controllers);
  }

  /**
   * Sets the Settings object this factory will provide.
   */
  @Override
  public void setSettingsInstance(Settings settings) {
    if (settingsInstance != null) {
      throw new RuntimeException("Settings instance has already been set");
    }
    this.settingsInstance = settings;
  }

  /**
   * Retrieves a motor based on the port.
   */
  @Override
  public Consumer<ControllerEvent> motor(int port) {
    if (!this.controllers.containsKey(port)) {
      throw new RuntimeException("Motor not registered");
    }
    return this.controllers.get(port);
  }

  /**
   * Gets the Settings subsystem instance.
   */
  @Override
  public Settings settingsSubsystem() {
    if (settingsInstance == null) {
      throw new RuntimeException("Settings instance hasn't been set");
    }
    return this.settingsInstance;
  }
}
