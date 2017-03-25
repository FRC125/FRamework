package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.profiling.MotionDeserialzer;
import com.nutrons.framework.util.Pair;
import io.reactivex.Flowable;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Created by Josh on 3/25/2017.
 */
public class TestMotionDeserializer {
  @Test
  public void testMotionDeserializer() {
    Reader r = new StringReader("path\n"
    + "2 \n"
    + "0 0\n"
    + "1 0\n"
    + "2 0\n"
    + "3 0\n");
    Flowable<Pair<Double, Double>> velocities = MotionDeserialzer.read(r);

    assertTrue(velocities.blockingFirst().left() == 0);
    assertTrue(velocities.blockingFirst().right() == 2);

    assertTrue(velocities.skip(1).blockingFirst().left() == 1);
    assertTrue(velocities.skip(1).blockingFirst().right() == 3);
  }
}
