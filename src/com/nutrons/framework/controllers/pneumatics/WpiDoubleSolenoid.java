package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WpiDoubleSolenoid {
    private final DoubleSolenoid comp;

    /**
     * Creates a double solenoid from given port
     */
    WpiDoubleSolenoid(int forwardChannel, int reverseChannel){
        this.comp = new DoubleSolenoid(forwardChannel, reverseChannel);
    }

    public void set(DoubleSolenoid.Value value){
        comp.set(value);
    }
}
