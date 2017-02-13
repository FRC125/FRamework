package com.nutrons.framework.controllers.pneumatics;

public class StopCompressorEvent implements PneumaticEvent {
    public StopCompressorEvent(){

    }

    public void actOn(WpiCompressor comp){
        comp.start();
    }
}
