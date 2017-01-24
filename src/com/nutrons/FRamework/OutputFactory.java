package com.nutrons.FRamework;

import com.nutrons.FRamework.consumers.ControllerEvent;
import com.nutrons.FRamework.subsystems.Settings;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.Map;


public class OutputFactory {

  private static OutputFactory instance;
  private Settings settingsInstance;

  private Map<Integer, Consumer<ControllerEvent>> controllers;

  private OutputFactory() {
    this.controllers = new HashMap<>();
  }

  /**
   * Get or create the singleton instance.
   *
   * @return The singleton instance
   */
  public static synchronized OutputFactory instance() {
    if (OutputFactory.instance == null) {
      OutputFactory.instance = new OutputFactory();
    }
    return OutputFactory.instance;
  }

  /**
   * Sets the type of speed controller on each port.
   *
   * @param controllers Mapping from port number to controller
   */
  public void setControllers(Map<Integer, Consumer<ControllerEvent>> controllers) {
    this.controllers.clear();
    this.controllers.putAll(controllers);
  }

  /**
   * Sets the Settings object this factory will provide.
   */
  public void setSettingsInstance(Settings settings) {
    if (settingsInstance != null) {
      throw new RuntimeException("Settings instance has already been set");
    }
    this.settingsInstance = settings;
  }

  /**
   * Retrieves a motor based on the port.
   */
  public Consumer<ControllerEvent> motor(int port) {
    if (!this.controllers.containsKey(port)) {
      throw new RuntimeException("Motor not registered");
    }
    return this.controllers.get(port);
  }

  /**
   * Gets the Settings subsystem instance.
   */
  public Settings settingsSubsystem() {
    if (settingsInstance == null) {
      throw new RuntimeException("Settings instance hasn't been set");
    }
    return this.settingsInstance;
  }
}
