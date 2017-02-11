package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Solenoid;

public class WpiSolenoid {
    private final Solenoid solenoid;

    /**
     * Creates a solenoid from given port
     * @param port PCM device ID
     */
    WpiSolenoid(int port){
        this.solenoid = new Solenoid(port);
    }

    public void set(boolean on){
        solenoid.set(on);
    }
}
