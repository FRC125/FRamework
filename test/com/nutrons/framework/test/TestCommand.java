package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.util.Command;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
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
    waitForDisposable(command.execute());
    assertTrue(arr[0] == 10);
  }

  @Test
  public void inSeriesTimed() {
    long start = System.currentTimeMillis();
    Command series = wait.endsWhen(() -> System.currentTimeMillis() - 2000 > start).then(wait);
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

  private void waitForDisposable(Disposable d) {
    Flowable.interval(30, TimeUnit.MILLISECONDS)
        .takeWhile(x -> !d.isDisposed()).blockingSubscribe();
  }
}
