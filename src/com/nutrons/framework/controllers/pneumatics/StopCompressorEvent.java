package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Compressor;

public class StopCompressorEvent implements PneumaticEvent {
    public StopCompressorEvent(){

    }

    public void actOn(Compressor comp){
        comp.start();
    }
}
