package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.Joystick;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;


public class InputFactory {

  private static InputFactory instance;

  private InputFactory() {
    this.joysticks = new HashMap<>();
  }

  /**
   * Get or create the singleton instance.
   *
   * @return The singleton instance
   */
  public static synchronized InputFactory instance() {
    if (InputFactory.instance == null) {
      InputFactory.instance = new InputFactory();
    }
    return InputFactory.instance;
  }

  private Map<Integer, Flowable<Pair<Double, Double>>> joysticks;

  private Flowable<Pair<Double, Double>> lazyJoy(int port) {
    if (!this.joysticks.containsKey(port)) {
      Joystick joystick = new Joystick(port);
      this.joysticks.put(port,
          Util.toFlow(() -> new Pair<>(joystick.getX(), joystick.getY())));
    }
    return this.joysticks.get(port);
  }

  public Flowable<Double> joystickX(int instance) {
    return lazyJoy(instance).map(Pair::left);
  }

  public Flowable<Double> joystickY(int instance) {
    return lazyJoy(instance).map(Pair::right);
  }
}
