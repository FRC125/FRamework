package com.nutrons.FRamework.test;

import com.nutrons.FRamework.Util;
import io.reactivex.Flowable;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TestFilter {

    @Test
    public void testFilterChange() {
        Flowable<Integer> o = Flowable.just(1, 1, 2, 2, 3, 2);
        List<Integer> result = Util.changedValues(o).toList().blockingGet();
        int[] assertResult = new int[]{1, 2, 3, 2};
        assertTrue(assertResult.length == result.size());
        for (int i = 0; i < assertResult.length; i++)
            assertTrue(result.get(i).equals(assertResult[i]));
    }

}
