package com.nutrons.framework.test;

import com.nutrons.framework.commands.Command;
import com.nutrons.framework.commands.Terminator;
import com.nutrons.framework.commands.TerminatorWrapper;
import io.reactivex.Flowable;
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

  static void waitForCommand(Flowable<Terminator> commandExecution) {
    commandExecution.blockingSubscribe();
  }

  @Before
  public void setupCommands() {
    delay = Command.fromAction(() -> {

    }).killAfter(1000, TimeUnit.MILLISECONDS);
  }

  @Test
  public void single() {
    final Integer[] arr = new Integer[1];
    arr[0] = 5;
    Command command = Command.fromAction(() -> arr[0] = 10);
    // Tests to see if this single command works.
    waitForCommand(command.execute(true));
    assertTrue(arr[0] == 10);
  }

  @Test
  public void testDelay() {
    long start = System.currentTimeMillis();
    waitForCommand(delay.execute(true));
    assertTrue(System.currentTimeMillis() - 1000 > start);
    assertTrue(System.currentTimeMillis() - 2000 < start);
  }

  @Test
  public void inSeriesTimed() {
    long start = System.currentTimeMillis();
    Command series = delay.then(delay);
    waitForCommand(series.execute(true));
    assertTrue(System.currentTimeMillis() - 2000 > start);
    assertTrue(System.currentTimeMillis() - 3000 < start);
  }

  @Test
  public void inParallelTimed() {
    long start = System.currentTimeMillis();
    Command para = Command.parallel(delay, delay);
    waitForCommand(para.execute(true));
    assertTrue(System.currentTimeMillis() - 1400 < start);
  }

  @Test
  public void testTerminable() throws InterruptedException {
    long start = System.currentTimeMillis();
    PublishProcessor<Object> pp = PublishProcessor.create();
    Flowable<Terminator> d = serial(delay, delay, delay, delay)
        .terminable(pp).execute(true);
    Thread.sleep(3000);
    pp.onNext(new Object());
    pp.onComplete();
    waitForCommand(d);
    assertTrue(System.currentTimeMillis() - 4000 < start);
  }

  @Test
  public void testUntil() throws InterruptedException {
    int[] record = new int[2];
    assertTrue(record[0] == 0);
    long start = System.currentTimeMillis();
    Flowable<Terminator> d = Command.fromAction(() -> record[0] = 1).until(() -> record[1] == 1).execute(true);
    Flowable.timer(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(x -> record[1] = 1);
    waitForCommand(d);
    assertTrue(System.currentTimeMillis() - 1000 > start);
    assertTrue(record[0] == 1);
    record[0] = 0;
    Thread.sleep(1000);
    assertTrue(record[0] == 0);
  }

  @Test
  public void testStartable() {
    long start = System.currentTimeMillis();
    waitForCommand(Command.fromAction(() -> {
    })
        .startable(Flowable.timer(1, TimeUnit.SECONDS)).execute(true));
    assertTrue(System.currentTimeMillis() - 1000 > start);
  }

  @Test
  public void testWhen() throws InterruptedException {
    int[] record = new int[2];
    assertTrue(record[0] == 0);
    Flowable<Terminator> d = Command.fromAction(() -> record[0] = 1).when(() -> record[1] == 1).execute(true);
    Thread.sleep(1000);
    assertTrue(record[0] == 0);
    long start = System.currentTimeMillis();
    Flowable.timer(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(x -> record[1] = 1);
    waitForCommand(d);
    assertTrue(System.currentTimeMillis() - 1000 > start);
    assertTrue(record[0] == 1);
  }

  @Test
  public void parallelAndSerial() {
    long start = System.currentTimeMillis();
    waitForCommand(parallel(delay.then(delay), delay).execute(true));
    assertTrue(System.currentTimeMillis() - 2000 > start);
    assertTrue(System.currentTimeMillis() - 3000 < start);
  }

  @Test
  public void killAfter() throws InterruptedException {
    int[] record = new int[1];
    long start = System.currentTimeMillis();
    Command.just(x -> Flowable.just(() -> {
      assertTrue(System.currentTimeMillis() - 2000 < start);
      record[0] = 1;
    })).killAfter(1, TimeUnit.SECONDS).execute(true);
    Thread.sleep(4000);
    assertTrue(record[0] == 1);
  }

  @Test
  public void testSwitch() throws InterruptedException {
    int[] record = new int[1];
    record[0] = 0;
    Command inc = Command.just(x -> {
      synchronized (record) {
        record[0] += 1;
      }
      return Flowable.just(new TerminatorWrapper(() -> {
        synchronized (record) {
          record[0] -= 1;
        }
      }));
    });
    Command.fromSwitch(Flowable.interval(1, TimeUnit.SECONDS).map(x -> inc).take(5))
        .execute(true).blockingSubscribe(Terminator::run);
    Thread.sleep(2000);
    assertTrue(record[0] == 0);
  }
}
