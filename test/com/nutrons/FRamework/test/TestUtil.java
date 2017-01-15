package com.nutrons.FRamework.test;

import com.nutrons.FRamework.Util;
import io.reactivex.Flowable;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TestUtil {

    @Test
    public void changedValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Flowable<Integer> o = Flowable.just(1, 1, 2, 2, 3, 2);
        Method m = Util.class.getDeclaredMethod("changedValues", Flowable.class);
        m.setAccessible(true);
        List<Integer> result = ((Flowable<Integer>) m.invoke(Util.class, o)).toList().blockingGet();
        int[] assertResult = new int[]{1, 2, 3, 2};
        assertTrue(assertResult.length == result.size());
        for (int i = 0; i < assertResult.length; i++)
            assertTrue(result.get(i).equals(assertResult[i]));
    }

}
