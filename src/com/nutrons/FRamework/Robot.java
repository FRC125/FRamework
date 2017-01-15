package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.RobotBase;
import io.reactivex.Flowable;

import static com.nutrons.FRamework.CompMode.*;

public class Robot extends RobotBase {

    private Flowable<Boolean> enabled;
    private Flowable<CompMode> mode;

    public Robot() {
        this.enabled = Util.changedValues(Util.toFlow(this::isEnabled));
        this.mode = Util.changedValues(Flowable.merge(
                Util.toFlow(this::isAutonomous).filter(x -> x).map((x) -> AUTO),
                Util.toFlow(this::isOperatorControl).filter(x -> x).map((x) -> TELEOP),
                Util.toFlow(this::isTest).filter(x -> x).map((x) -> TEST)));
    }

    @Override
    public void startCompetition() {
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