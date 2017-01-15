package com.nutrons.FRamework;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Util {
    /**
     * Generate a Flowable from a periodic call to a Supplier.
     *
     * @param period the number of time units to wait before calling the supplier again
     * @param <T>    the type of the Flowable and Supplier
     */
    static <T> Flowable<T> toFlow(Supplier<T> s, long period, TimeUnit unit) {
        return Flowable.interval(period, unit, Schedulers.io()).subscribeOn(Schedulers.io()).map((x) -> s.get());
    }

    /**
     * Generate a Flowable from a periodical call to a Supplier.
     *
     * @param <T> the type of the Flowable and Supplier
     */
    static <T> Flowable<T> toFlow(Supplier<T> s) {
        return toFlow(s, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * @return A Flowable of all items that are not equal to their predecessor, including the initial item.
     */
    static <T> Flowable<T> changedValues(Flowable<T> o) {
        Flowable<List<T>> asymmetricPairs = o.buffer(2, 1).filter((x) -> x.size() == 2 && !x.get(0).equals(x.get(1)));
        return o.take(1).concatWith(asymmetricPairs.map((x) -> x.get(1)));
    }
}
