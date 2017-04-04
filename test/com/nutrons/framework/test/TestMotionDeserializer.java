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
        + "1 2 3 4 5 6\n"
        + "7 8 9 10 11 12\n"
        + "13 14 15 16 17 18\n"
        + "19 20 21 22 23 24\n");
    Flowable<Pair<Double[], Double[]>> velocities = MotionDeserialzer.read(r);

    Double[] left1 = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    Double[] left2 = {7.0, 8.0, 9.0, 10.0, 11.0, 12.0};

    Double[] right1 = {13.0, 14.0, 15.0, 16.0, 17.0, 18.0};
    Double[] right2 = {19.0, 20.0, 21.0, 22.0, 23.0, 24.0};

    assertTrue(isSamesies(velocities.blockingFirst().left(), left1));
    assertTrue(isSamesies(velocities.blockingFirst().right(), right1));

    assertTrue(isSamesies(velocities.skip(1).blockingFirst().left(), left2));
    assertTrue(isSamesies(velocities.skip(1).blockingFirst().right(), right2));
  }

  boolean isSamesies(Double[] a, Double[] b) {
    for (int i = 0; i < a.length; i++) {
      if (!a[i].equals(b[i])) {
        return false;
      }
    }
    return true;
  }
}
