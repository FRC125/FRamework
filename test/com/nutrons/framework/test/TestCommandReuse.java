package com.nutrons.framework.test;

import com.nutrons.framework.commands.Command;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.nutrons.framework.commands.Command.parallel;
import static com.nutrons.framework.test.TestCommand.waitForCommand;
import static junit.framework.TestCase.assertTrue;

public class TestCommandReuse {

  @Test
  public void testEndConditionReuse() {
    int[] record = new int[2];
    record[0] = 0; // this is the value that the command will change
    record[1] = 0; // if 1, the command will end.
    Command change = Command.create(() -> record[0] = 1).until(() -> record[1] == 1);
    waitForCommand(parallel(change, Command.create(() -> record[1] = 1)
        .delayStart(2, TimeUnit.SECONDS)).execute());
    assertTrue(record[0] == 1);
    // now we try it again.
    record[0] = 0;
    record[1] = 0;
    long start = System.currentTimeMillis();
    change.killAfter(1, TimeUnit.SECONDS).execute().blockingSubscribe();
    // assert that command only quit because 1 second passed, proving that the command is reusable.
    assertTrue(start + 1000 < System.currentTimeMillis());
    // assert that command still functioned
    assertTrue(record[0] == 1);
  }
}
