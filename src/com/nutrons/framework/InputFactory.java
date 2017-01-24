package com.nutrons.framework;

import com.nutrons.framework.util.FlowOperators;
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

  private Map<Integer, Joystick> joysticks;

  private Flowable<Double> memoizedJoy(int port, int axis) {
    if (!this.joysticks.containsKey(port)) {
      Joystick joystick = new Joystick(port);
      this.joysticks.put(port,
          joystick);
    }
    return FlowOperators.toFlow(() -> this.joysticks.get(port).getRawAxis(axis));
  }

  public Flowable<Double> controllerX(int instance) {
    return memoizedJoy(instance, 0);
  }

  public Flowable<Double> controllerX2(int instance) {
    return memoizedJoy(instance, 4);
  }

  public Flowable<Double> controllerY(int instance) {
    return memoizedJoy(instance, 1);
  }

  public Flowable<Double> controllerY2(int instance) {
    return memoizedJoy(instance, 5);
  }
}
