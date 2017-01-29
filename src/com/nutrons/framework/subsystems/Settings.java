package com.nutrons.framework.subsystems;

import com.nutrons.framework.Subsystem;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Preferences;
import io.reactivex.Flowable;

public class Settings implements Subsystem {

    public Flowable<Double> getProperty(String key) {
        return FlowOperators.toFlow( () ->Preferences.getInstance().getDouble(key, 0.0) ).distinctUntilChanged();
        //TODO: don't emit anything if it's 0.0, store last value and send that as backup
    }

    @Override
    public void registerSubscriptions() {

    }
}