package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Solenoid;

public class SetSolenoidEvent implements PneumaticEvent {
    private final boolean on;

    public SetSolenoidEvent(boolean on) {
        this.on = on;
    }

    /**
     * Set a solenoid
     *
     * @param solenoid Solenoid to apply event to.
     */
    public void actOn(WpiSolenoid solenoid) {
        solenoid.set(on);
    }
}
