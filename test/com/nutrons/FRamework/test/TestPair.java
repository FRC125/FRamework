package com.nutrons.FRamework.test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

import com.nutrons.FRamework.Pair;
import org.junit.Before;
import org.junit.Test;

public class TestPair {

  public Pair<Integer, String> pair;

  @Before
  public void initialize() {
    pair = new Pair<>(1, "b");
  }

  @Test
  public void checkString() {
    assertEquals(pair.toString(), "( 1, b )");
  }

  @Test
  public void checkEquality() {
    assertEquals(pair, new Pair<Integer, String>(1, "b"));
  }

  @Test
  public void checkInequality() {
    assertFalse(pair.equals(new Pair<Integer, String>(2, "b")));
  }
}
