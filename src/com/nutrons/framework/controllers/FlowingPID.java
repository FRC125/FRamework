package com.nutrons.framework.controllers;

import io.reactivex.Flowable;

import static io.reactivex.Flowable.combineLatest;

/**
 * Created by Josh on 2/11/2017.
 */
public class FlowingPID {
    private double PROPORTIONAL;
    private double INTEGRAL;
    private double DERIVATIVE;
    private Flowable<Double>  error;
    private Flowable<Double> controlOutput;

    public FlowingPID(Flowable<Double> error, double p, double i, double d) {
        this.PROPORTIONAL = p;
        this.INTEGRAL = i;
        this.DERIVATIVE = d;
        this.error = error;
        Flowable<Double> errorP = error.map(x -> x * PROPORTIONAL);
        Flowable<Double> errorI = error.buffer(10, 1).map(list -> list.stream().reduce(0.0, (x, acc) -> x + acc)).map(x -> x * INTEGRAL);
        Flowable<Double> errorD = error.buffer(2, 1).map(last -> last.stream().reduce(0.0, (x, y) -> x - y)).map(x -> x * DERIVATIVE);
        this.controlOutput = combineLatest(errorP, errorI, errorD, (pr, in, de) -> pr + in + de);
    }

    public Flowable<Double> getOutput() {
        return controlOutput;
    }

}
