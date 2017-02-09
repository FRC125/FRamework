package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.util.Command;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

public class TestCommand {
  private Command wait;

  @Before
  public void setupCommands() {
    wait = Command.create(() -> {
      Thread.sleep(1000);
    });
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
  public void inSeriesTimed() {
    long start = System.currentTimeMillis();
    Command series = wait.then(wait);
   waitForDisposable(series.execute());
    assertTrue(System.currentTimeMillis() - 2000 > start);
  }

  @Test
  public void inParallelTimed() {
    long start = System.currentTimeMillis();
    Command para = Command.parallel(wait, wait);
    waitForDisposable(para.execute());
    assertTrue(System.currentTimeMillis() - 1400 < start);
  }

  @Test
  public void testTerminable() throws InterruptedException {
    PublishProcessor pp = PublishProcessor.create();
    Disposable d = Command.create(Flowable.interval(100, TimeUnit.MILLISECONDS).map(x -> () -> {}))
        .terminable(pp).execute();
    Thread.sleep(3000);
    pp.onComplete();
    waitForDisposable(d);
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
    waitForDisposable(Command.create(() -> {})
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

  static void waitForDisposable(Disposable d) {
    Flowable.interval(30, TimeUnit.MILLISECONDS)
        .takeWhile(x -> !d.isDisposed()).blockingSubscribe();
  }
}
