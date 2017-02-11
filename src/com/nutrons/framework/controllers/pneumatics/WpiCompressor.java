package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Compressor;

public class WpiCompressor {
    private final Compressor comp;

    /**
     * Creates a compressor from given port
     * @param port CAN device ID
     */
    WpiCompressor(int port){
        this.comp = new Compressor(port);
    }

    public void start(){
        this.comp.start();
    }

    public void stop(){
        this.comp.stop();
    }
}
