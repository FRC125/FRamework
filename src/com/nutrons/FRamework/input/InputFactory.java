package com.nutrons.FRamework.input;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import io.reactivex.Flowable;

public class InputFactory {
    
    public <T> Flowable<T> toFlow(Supplier<T> s, long period, TimeUnit unit) {
	return Flowable.interval(period, unit).map((x)->s.get());
    }
    
}
