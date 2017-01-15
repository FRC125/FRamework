package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.Joystick;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;

import static edu.wpi.first.wpilibj.Joystick.AxisType.kX;
import static edu.wpi.first.wpilibj.Joystick.AxisType.kY;

public class InputFactory {

    private static InputFactory instance;

    private InputFactory() {
        this.joysticks = new HashMap<>();
    }

    public static synchronized InputFactory instance() {
        if (InputFactory.instance == null) {
            InputFactory.instance = new InputFactory();
        }
        return InputFactory.instance;
    }

    private Map<Integer, Flowable<Pair<Double, Double>>> joysticks;

    private Flowable<Pair<Double, Double>> lazyJoy(int port) {
        if (!this.joysticks.containsKey(port)) {
            Joystick j = new Joystick(port);
            this.joysticks.put(port,
                    Util.toFlow(() -> new Pair<>(j.getAxis(kX), j.getAxis(kY))));
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
