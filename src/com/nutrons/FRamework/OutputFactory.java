package com.nutrons.FRamework;

import com.nutrons.FRamework.consumers.LoopControllerEvent;
import java.util.HashMap;
import java.util.Map;
import org.reactivestreams.Subscriber;


public class OutputFactory {

  private static OutputFactory instance;

  private Map<Integer, Subscriber<LoopControllerEvent>> controllers;

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
  public void setControllers(Map<Integer, Subscriber<LoopControllerEvent>> controllers) {
    this.controllers.clear();
    this.controllers.putAll(controllers);
  }

  /**
   * Retrieves a motor based on the port.
   */
  public Subscriber<LoopControllerEvent> motor(int port) {
    if (!this.controllers.containsKey(port)) {
      throw new RuntimeException("Motor not registered");
    }
    return this.controllers.get(port);
  }
}
