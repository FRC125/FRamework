package com.nutrons.framework.test;

import static com.nutrons.framework.commands.Command.parallel;
import static com.nutrons.framework.commands.Command.serial;
import static com.nutrons.framework.test.TestCommand.waitForCommand;
import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.commands.Command;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class TestCommandReuse {

  @Test
  public void testEndConditionReuse() {
    int[] record = new int[2];
    record[0] = 0; // this is the value that the command will change
    record[1] = 0; // if 1, the command will end.
    Command change = Command.fromAction(() -> record[0] = 1).until(() -> record[1] == 1);
    waitForCommand(parallel(change, Command.fromAction(() -> record[1] = 1)
        .delayStart(2, TimeUnit.SECONDS)).execute());
    assertTrue(record[0] == 1);
    // now we try it again.
    record[0] = 0;
    record[1] = 0;
    long start = System.currentTimeMillis();
    change.killAfter(1, TimeUnit.SECONDS).execute().blockingSubscribe();
    // assert that command only quit because 1 second passed, proving that the command is reusable.
    assertTrue(start + 900 < System.currentTimeMillis());
    // assert that command still functioned
    assertTrue(record[0] == 1);
  }

  @Test
  public void testIncrementReuse() throws InterruptedException {
    int[] record = new int[1];
    Command inc = Command.fromAction(() -> {
      synchronized (record) {
        record[0] += 1;
      }
    });
    serial(inc, inc, inc).execute();
    Thread.sleep(3000);
    assertTrue(record[0] == 3);
    inc.execute();
    Thread.sleep(1000);
    assertTrue(record[0] == 4);
  }
}
