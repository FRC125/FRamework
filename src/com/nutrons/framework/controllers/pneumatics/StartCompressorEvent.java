package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Compressor;

public class StartCompressorEvent implements PneumaticEvent {
    public StartCompressorEvent(){

    }

    public void actOn(Compressor comp){
        comp.start();
    }
}
