package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.SpeedController;
import io.reactivex.functions.Consumer;

import java.util.HashMap;
import java.util.Map;

public class OutputFactory {

    private static OutputFactory instance;

    private Map<Integer, SpeedController> controllers;
    private Map<Integer, Consumer<Double>> motorSpeedConsumer;

    private OutputFactory() {
        this.controllers = new HashMap<>();
        this.motorSpeedConsumer = new HashMap<>();
    }

    public static OutputFactory instance() {
        if (OutputFactory.instance == null) {
            OutputFactory.instance = new OutputFactory();
        }
        return OutputFactory.instance;
    }

    public void setControllers(Map<Integer, SpeedController> controllers) {
        this.controllers.clear();
        this.motorSpeedConsumer.clear();
        this.controllers.putAll(controllers);
    }

    private void lazySpeedConsumer(int instance) {
        if (!motorSpeedConsumer.containsKey(instance)) {
            this.motorSpeedConsumer.put(instance, (x) -> this.controllers.get(instance).set(x));
        }
    }

    public Consumer<Double> motor(int instance) {
        lazySpeedConsumer(instance);
        return this.motorSpeedConsumer.get(instance);
    }
}
