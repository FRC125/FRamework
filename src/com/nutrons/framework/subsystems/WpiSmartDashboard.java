package com.nutrons.framework.subsystems;

import com.nutrons.framework.Subsystem;
import io.reactivex.functions.Consumer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WpiSmartDashboard implements Subsystem{

    public Consumer<Double> getProducer(String key){
        return d -> SmartDashboard.putNumber(key, d);
    }

    @Override
    public void registerSubscriptions() {
        //Left intentionally empty
    }
}
