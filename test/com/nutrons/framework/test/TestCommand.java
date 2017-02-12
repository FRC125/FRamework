package com.nutrons.framework.test;

import com.nutrons.framework.commands.Command;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.nutrons.framework.commands.Command.parallel;
import static com.nutrons.framework.commands.Command.serial;
import static junit.framework.TestCase.assertTrue;

public class TestCommand {
  private Command delay;

  @Before
  public void setupCommands() {
    delay = Command.create(() -> {

    }).delayFinish(1000, TimeUnit.MILLISECONDS);
  }

  @Test
  public void single() {
    final Integer[] arr = new Integer[1];
    arr[0] = 5;
    Command command = Command.create(() -> arr[0] = 10);
    // Tests to see if this single command works.
    waitForDisposable(command.execute());
    assertTrue(arr[0] == 10);
  }

  @Test
  public void testDelay() {
    long start = System.currentTimeMillis();
    waitForDisposable(delay.execute());
    assertTrue(System.currentTimeMillis() - 1000 > start);
  }

  @Test
  public void inSeriesTimed() {
    long start = System.currentTimeMillis();
    Command series = delay.then(delay);
    waitForDisposable(series.execute());
    assertTrue(System.currentTimeMillis() - 2000 > start);
  }

  @Test
  public void inParallelTimed() {
    long start = System.currentTimeMillis();
    Command para = Command.parallel(delay, delay);
    waitForDisposable(para.execute());
    assertTrue(System.currentTimeMillis() - 1400 < start);
  }

  @Test
  public void testTerminable() throws InterruptedException {
    long start = System.currentTimeMillis();
    PublishProcessor pp = PublishProcessor.create();
    Disposable d = serial(delay, delay, delay, delay)
        .terminable(pp).execute();
    Thread.sleep(3000);
    pp.onComplete();
    waitForDisposable(d);
    assertTrue(System.currentTimeMillis() - 4000 < start);
  }

  @Test
  public void testUntil() throws InterruptedException {
    int[] record = new int[2];
    assertTrue(record[0] == 0);
    long start = System.currentTimeMillis();
    Disposable d = Command.create(() -> record[0] = 1).until(() -> record[1] == 1).execute();
    Flowable.timer(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(x -> record[1] = 1);
    waitForDisposable(d);
    assertTrue(System.currentTimeMillis() - 1000 > start);
    assertTrue(record[0] == 1);
    record[0] = 0;
    Thread.sleep(1000);
    assertTrue(record[0] == 0);
  }

  @Test
  public void testStartable() {
    long start = System.currentTimeMillis();
    waitForDisposable(Command.create(() -> {
    })
        .startable(Flowable.timer(1, TimeUnit.SECONDS)).execute());
    assertTrue(System.currentTimeMillis() - 1000 > start);
  }

  @Test
  public void testWhen() throws InterruptedException {
    int[] record = new int[2];
    assertTrue(record[0] == 0);
    Disposable d = Command.create(() -> record[0] = 1).when(() -> record[1] == 1).execute();
    Thread.sleep(1000);
    assertTrue(record[0] == 0);
    long start = System.currentTimeMillis();
    Flowable.timer(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(x -> record[1] = 1);
    waitForDisposable(d);
    assertTrue(System.currentTimeMillis() - 1000 > start);
    assertTrue(record[0] == 1);
  }

  @Test
  public void parallelAndSerial() {
    long start = System.currentTimeMillis();
    waitForDisposable(parallel(delay.then(delay), delay).execute());
    assertTrue(System.currentTimeMillis() - 2000 > start);
    assertTrue(System.currentTimeMillis() - 3000 < start);
  }

  static void waitForDisposable(Disposable d) {
    Flowable.interval(30, TimeUnit.MILLISECONDS)
        .takeWhile(x -> !d.isDisposed()).blockingSubscribe();
  }
}
