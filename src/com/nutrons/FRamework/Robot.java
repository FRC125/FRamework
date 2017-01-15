package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.RobotBase;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class Robot extends RobotBase {

    private List<Subsystem> subsystems;

    public Robot() {
        this.subsystems = new ArrayList<>();
    }

    /**
     * Register a subsystem, so it will only subscribe side effect consumers (such as motors) once the Robot is initialized.
     */
    protected void registerSubsystem(Subsystem subsystem) {
        this.subsystems.add(subsystem);
    }

    @Override
    public final void startCompetition() {
        Observable.fromIterable(this.subsystems).blockingSubscribe(s -> s.registerStreams());
    }
}