package com.nutrons.framework.factories;

/**
 * Subsystems use this for retrieving the OutputFactory instance.
 * Bootstrappers set the OutputFactory instance here prior to the instantiation of subsystems.
 */
public class OutputManager {
  private static OutputFactory factory;

  /**
   * Set the OutputFactory instance.
   * Throws exception if one has already been set.
   *
   * @param factory the OutputFactory instance
   */
  public static synchronized void setFactory(OutputFactory factory) {
    if (OutputManager.factory != null) {
      throw new RuntimeException("The default OutputFactory has ALREADY been set!");
    }
    OutputManager.factory = factory;
  }

  /**
   * Get the OutputFactory instance.
   * Throws exception if none has been set.
   *
   * @return the OutputFactory instance
   */
  public static synchronized OutputFactory factory() {
    if (factory == null) {
      throw new RuntimeException("The default OutputFactory HAS NOT been set!");
    }
    return factory;
  }
}
