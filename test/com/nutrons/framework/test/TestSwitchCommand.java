package com.nutrons.framework.test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.commands.Command;
import com.nutrons.framework.commands.TerminatorWrapper;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class TestSwitchCommand {

  @Test
  public void testSwitchStart() throws InterruptedException {
    List<Long> list = new ArrayList<>();
    Flowable<Command> commandStream = Flowable.interval(100, TimeUnit.MILLISECONDS)
        .map(x -> putNumber(list, x)).take(5);
    Command.fromSwitch(commandStream, true).execute(true);
    Thread.sleep(300);
    assertFalse(list.contains(4));
    Thread.sleep(500);
    for (long i = 0; i < 5; i++) {
      assertTrue(list.contains(i));
    }
  }

  private Command putNumber(List<Long> list, long number) {
    return Command.fromAction(() -> list.add(number));
  }


  @Test
  public void testSwitchStartAndStop() throws InterruptedException {
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
    Command.fromSwitch(Flowable.interval(1, TimeUnit.SECONDS).map(x -> inc).take(5), true)
        .execute(true).blockingSubscribe();
    Thread.sleep(2000);
    assertTrue(record[0] == 0);
  }

  @Test
  public void testSwitchTerminateRealtime() throws InterruptedException {
    int[] record = new int[1];
    long start = System.currentTimeMillis();
    record[0] = 0;
    Command inc = Command.just(x -> Flowable.just(new TerminatorWrapper(() -> {
      synchronized (record) {
        record[0] += 1;
      }
    })));
    Command.fromSwitch(Flowable.interval(1, TimeUnit.SECONDS).map(x -> inc).take(5), true)
        .execute(true);
    assertTrue(System.currentTimeMillis() - start < 1000);
    Thread.sleep(2000);
    assertTrue(record[0] < 5);
    assertTrue(record[0] > 0);
  }

  @Test
  public void testSwitchNotTerminating() throws InterruptedException {
    Command doesntFinish = Command.just(x -> Flowable.just(() -> assertTrue(false)));
    doesntFinish.execute(false);
    Thread.sleep(1000);
    Command justOne = Command.fromSwitch(Flowable.<Command>never()
        .mergeWith(Flowable.just(doesntFinish)), false);
    justOne.execute(false);
    Thread.sleep(1000);
    justOne.execute(true);
    Thread.sleep(1000);
  }

  @Test(expected = RuntimeException.class)
  public void testSwitchTerminatesOnNext() {
    Command doesntFinish = Command.just(x -> Flowable.just(() -> {
      throw new RuntimeException();
    }));
    Command two = Command.fromSwitch(Flowable.just(doesntFinish, doesntFinish), false);
    two.execute(false).blockingSubscribe();
  }
}
