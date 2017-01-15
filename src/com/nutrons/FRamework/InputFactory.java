package com.nutrons.FRamework;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Joystick;

import io.reactivex.Flowable;

public class InputFactory {

    private static InputFactory instance;

    private Map<Integer, Joystick> joysticks;
    private Map<Integer, Flowable<Double>> joysticksX;
    private Map<Integer, Flowable<Double>> joysticksY;

    private InputFactory() {
        this.joysticks = new HashMap<>();
        this.joysticksX = new HashMap<>();
        this.joysticksY = new HashMap<>();
    }

    public static InputFactory instance() {
        if (InputFactory.instance == null) {
            InputFactory.instance = new InputFactory();
        }
        return InputFactory.instance;
    }

    private Joystick lazyJoy(int instance) {
        if (!this.joysticks.containsKey(instance)) {
            Joystick j = new Joystick(instance);
            this.joysticks.put(instance, j);
            this.joysticksX.put(instance, Util.toFlow(() -> j.getAxis(Joystick.AxisType.kX)));
            this.joysticksY.put(instance, Util.toFlow(() -> j.getAxis(Joystick.AxisType.kY)));
        }
        return this.joysticks.get(instance);
    }

    public Flowable<Double> joystickX(int instance) {
        lazyJoy(instance);
        return this.joysticksX.get(instance);
    }

    public Flowable<Double> joystickY(int instance) {
        lazyJoy(instance);
        return this.joysticksY.get(instance);
    }
}
