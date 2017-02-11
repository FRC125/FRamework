package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class SetDoubleSolenoidEvent implements PneumaticEvent {
    private final DoubleSolenoid.Value value;

    public SetDoubleSolenoidEvent(DoubleSolenoid.Value value) {
        this.value = value;
    }

    /**
     * Set the value of a double solenoid
     *
     * @param doubleSolenoid Double Solenoid to apply event to.
     */
    public void actOn(DoubleSolenoid doubleSolenoid) {
        doubleSolenoid.set(value);
    }
}
