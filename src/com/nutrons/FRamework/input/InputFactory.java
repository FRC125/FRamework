package com.nutrons.FRamework.input;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Joystick;
import io.reactivex.Flowable;
import io.reactivex.Observable;

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

    public <T> Flowable<T> toFlow(Supplier<T> s, long period, TimeUnit unit) {
        return Flowable.interval(period, unit).map((x) -> s.get());
    }

    private Joystick lazyJoy(int instance) {
        if (!this.joysticks.containsKey(instance)) {
            Joystick j = new Joystick(instance);
            this.joysticks.put(instance, j);
            this.joysticksX.put(instance, this.toFlow(() -> j.getAxis(Joystick.AxisType.kX), 100, TimeUnit.MILLISECONDS));
            this.joysticksY.put(instance, this.toFlow(() -> j.getAxis(Joystick.AxisType.kY), 100, TimeUnit.MILLISECONDS));
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
