package com.nutrons.FRamework.test;

import com.nutrons.FRamework.Pair;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class TestPair {

    public Pair<Integer, String> p;
    @Before
    public void initialize() {
        p = new Pair<>(1, "b");
    }

    @Test
    public void checkString() {
        assertEquals(p.toString(), "( 1, b )");
    }

    @Test
    public void checkEquality() {
        assertEquals(p, new Pair<Integer, String>(1, "b"));
    }

    @Test
    public void checkInequality() {
        assertFalse(p.equals(new Pair<Integer, String>(2, "b")));
    }
}
