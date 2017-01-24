package com.nutrons.framework.factories;

import com.nutrons.framework.consumers.ControllerEvent;
import com.nutrons.framework.consumers.LoopSpeedController;
import com.nutrons.framework.subsystems.Settings;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Joystick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WPIFactory implements InputFactory, OutputFactory {

  private Map<Integer, Joystick> joysticks;
  private Settings settingsInstance;

  private Map<Integer, Consumer<ControllerEvent>> controllers;

  public WPIFactory() {
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
  public Flowable<Double> controllerX(int instance) {
    return memoizedJoy(instance, 0);
  }

  @Override
  public Flowable<Double> controllerX2(int instance) {
    return memoizedJoy(instance, 4);
  }

  @Override
  public Flowable<Double> controllerY(int instance) {
    return memoizedJoy(instance, 1);
  }

  @Override
  public Flowable<Double> controllerY2(int instance) {
    return memoizedJoy(instance, 5);
  }

  /**
   * Sets the type of speed controller on each port.
   *
   * @param controllers Mapping from port number to controller
   */
  @Override
  public void setControllers(List<LoopSpeedController> controllers) {
    for (LoopSpeedController controller : controllers) {
      if (this.controllers.containsKey(controller.port())) {
        throw new RuntimeException("Controller on port "
            + controller.port() + " is already registered!");
      }
    }
    Observable.fromIterable(controllers)
        .blockingSubscribe(x -> this.controllers.put(x.port(), x));
  }

  @Override
  public void setController(LoopSpeedController controller) {
    if (controllers.containsKey(controller.port())) {
      throw new RuntimeException("Controller on port "
          + controller.port() + " is already registered!");
    }
    controllers.put(controller.port(), controller);
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
