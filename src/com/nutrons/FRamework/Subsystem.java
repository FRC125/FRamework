package com.nutrons.FRamework;

public abstract class Subsystem {

    /**
     * Subscribe side effect consumers (such as motors) to their sources.
     */
    public abstract void registerStreams();

}
