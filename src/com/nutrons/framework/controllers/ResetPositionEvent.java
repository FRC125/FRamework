package com.nutrons.framework.controllers;

import com.sun.org.glassfish.external.statistics.annotations.Reset;

public class ResetPositionEvent implements ControllerEvent {
    private final double position;

    public ResetPositionEvent(double position) {
        this.position = position;
    }

    /**
     * Reset the position of a Talon object
     *
     * @param talon Talon controller to apply event to.
     */
    public void actOn(Talon talon) {
        talon.resetPositionTo(position);
    }
}
