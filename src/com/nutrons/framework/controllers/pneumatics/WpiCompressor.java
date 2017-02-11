package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import io.reactivex.functions.Consumer;

public class WpiCompressor implements Consumer<PneumaticEvent> {
    private final Compressor comp;

    /**
     * Creates a compressor from given port
     * @param port CAN device ID
     */
    public WpiCompressor(int port){
        this.comp = new Compressor(port);
    }

    public void start(){
        this.comp.start();
    }

    public void stop(){
        this.comp.stop();
    }

    @Override
    public void accept(PneumaticEvent event) {
        event.actOn(this);
    }
}
