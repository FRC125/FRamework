package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.RobotBase;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

import static com.nutrons.FRamework.CompMode.*;

public class Robot extends RobotBase {

    private List<Subsystem> subsystems;
    private Flowable<Boolean> enabled;
    private Flowable<CompMode> mode;

    public Robot() {
        this.subsystems = new ArrayList<>();
        this.enabled = Util.changedValues(Util.toFlow(this::isEnabled));
        this.mode = Util.changedValues(Flowable.merge(
                Util.toFlow(this::isAutonomous).filter(x -> x).map((x) -> AUTO),
                Util.toFlow(this::isOperatorControl).filter(x -> x).map((x) -> TELEOP),
                Util.toFlow(this::isTest).filter(x -> x).map((x) -> TEST)));
    }

    /**
     * Register a subsystem, so it will only subscribe side effect consumers (such as motors) once the Robot is initialized.
     */
    protected void registerSubsystem(Subsystem subsystem) {
        this.subsystems.add(subsystem);
    }

    @Override
    public void startCompetition() {
        Observable.fromIterable(this.subsystems).blockingSubscribe(s -> s.registerStreams());
        this.enabled.ignoreElements().blockingAwait();
        this.mode.ignoreElements().blockingAwait();
    }

    /**
     * @return A Flowable representing the "enabled" state of the robot over time
     */
    public Flowable<Boolean> enabled() {
        return this.enabled;
    }

    /**
     * @return A Flowable representing the "competition mode" of the robot over time
     */
    public Flowable<CompMode> mode() {
        return this.mode;
    }
}