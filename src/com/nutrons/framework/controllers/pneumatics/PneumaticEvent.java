package com.nutrons.framework.controllers.pneumatics;

import com.nutrons.framework.controllers.EventUnimplementedException;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public interface PneumaticEvent {

  default void actOn(WpiDoubleSolenoid doubleSolenoid) {
    throw new EventUnimplementedException(
        doubleSolenoid.getClass().toString(), this.getClass().toString());
  }

  default void actOn(WpiSolenoid solenoid) {
    throw new EventUnimplementedException(
        solenoid.getClass().toString(), this.getClass().toString());
  }

  default void actOn(WpiCompressor compressor) {
    throw new EventUnimplementedException(
        compressor.getClass().toString(), this.getClass().toString());
  }
}

