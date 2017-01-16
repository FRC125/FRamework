package com.nutrons.FRamework;

import edu.wpi.first.wpilibj.RobotBase;
import io.reactivex.Flowable;

import static com.nutrons.FRamework.CompMode.*;

public class Robot extends RobotBase {

    private StreamManager sm;

    public Robot() {
        this.sm = new StreamManager(
                Util.changedValues(Util.toFlow(this::isEnabled)),
                Util.changedValues(Flowable.merge(
                    Util.toFlow(this::isAutonomous).filter(x -> x).map((x) -> AUTO),
                    Util.toFlow(this::isOperatorControl).filter(x -> x).map((x) -> TELEOP),
                    Util.toFlow(this::isTest).filter(x -> x).map((x) -> TEST)))
        );
    }

    @Override
    public final void startCompetition() {
        this.sm.startCompetition();
    }

    @Override
    public final void free() {
        super.free();
    }

    @Override
    public final boolean isDisabled() {
        return super.isDisabled();
    }

    @Override
    public final boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public final boolean isAutonomous() {
        return super.isAutonomous();
    }

    @Override
    public final boolean isTest() {
        return super.isTest();
    }

    @Override
    public final boolean isOperatorControl() {
        return super.isOperatorControl();
    }

    @Override
    public final boolean isNewDataAvailable() {
        return super.isNewDataAvailable();
    }
}