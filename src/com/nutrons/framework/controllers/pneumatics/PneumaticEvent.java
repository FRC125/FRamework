package com.nutrons.framework.controllers.pneumatics;

import com.nutrons.framework.controllers.EventUnimplementedException;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public interface PneumaticEvent {
    default void actOn(DoubleSolenoid doubleSolenoid) {
        throw new EventUnimplementedException(
                doubleSolenoid.getClass().toString(), this.getClass().toString());
    }

    default void actOn(Solenoid solenoid) {
        throw new EventUnimplementedException(
                solenoid.getClass().toString(), this.getClass().toString());
    }

    default void actOn(Compressor compressor) {
        throw new EventUnimplementedException(
                compressor.getClass().toString(), this.getClass().toString());
    }
}

