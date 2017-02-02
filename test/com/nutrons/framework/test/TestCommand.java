package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.Command;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

public class TestCommand {

  @Test
  public void single() throws Exception {
    final Integer[] arr = new Integer[1];
    arr[0] = 5;
    Command command = Command.create(() -> arr[0] = 10);
    command.stream().subscribe(Action::run);
    assertTrue(arr[0] == 10);
  }

  @Test
  public void inSeriesTimed() {
    long start = System.currentTimeMillis();
    Flowable<Action> actions = Command.createWithCondition(
        () -> Thread.sleep(1000), () -> System.currentTimeMillis() + 100 > start)
        .then(Command.createWithCondition(() -> Thread.sleep(1000),
            () -> System.currentTimeMillis() + 100 > start)).stream().take(2);
    assertTrue(actions.toList().blockingGet().size() == 2);
    long start2 = System.currentTimeMillis();
    actions.blockingSubscribe(Action::run);
    assertTrue(start2 + 1700 < System.currentTimeMillis());
  }

  @Test
  public void inParallelTimed() {
    long start = System.currentTimeMillis();
    Flowable<Action> actions = Command.parallel(Command.createWithCondition(
        () -> Thread.sleep(1000), () -> System.currentTimeMillis() + 100 > start),
        Command.createWithCondition(() -> Thread.sleep(1000),
            () -> System.currentTimeMillis() + 100 > start)).stream().take(2);
    assertTrue(actions.toList().blockingGet().size() == 2);
    long start2 = System.currentTimeMillis();
    actions.subscribeOn(Schedulers.io()).subscribe(Action::run);
    assertTrue(start2 + 1700 > System.currentTimeMillis());
  }
}
