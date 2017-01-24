package com.nutrons.framework.factories;

/**
 * Subsystems use this for retrieving the InputFactory instance.
 * Bootstrappers set the InputFactory instance here prior to the instantiation of subsystems.
 */
public class InputManager {
  private static InputFactory factory;

  /**
   * Set the InputFactory instance.
   * Throws exception if one has already been set.
   *
   * @param factory the InputFactory instance
   */
  public static synchronized void setFactory(InputFactory factory) {
    if (InputManager.factory != null) {
      throw new RuntimeException("The default InputFactory has ALREADY been set!");
    }
    InputManager.factory = factory;
  }

  /**
   * Get the InputFactory instance.
   * Throws exception if none has been set.
   *
   * @return the InputFactory instance
   */
  public static synchronized InputFactory factory() {
    if (factory == null) {
      throw new RuntimeException("The default InputFactory HAS NOT been set!");
    }
    return factory;
  }
}
