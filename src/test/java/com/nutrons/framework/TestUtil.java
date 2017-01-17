package com.nutrons.framework.test;

import static junit.framework.TestCase.assertTrue;

import com.nutrons.framework.Util;
import io.reactivex.Flowable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;

public class TestUtil {

  @Test
  public void changedValues() throws NoSuchMethodException,
      InvocationTargetException,
      IllegalAccessException {
    Flowable<Integer> flowable = Flowable.just(1, 1, 2, 2, 3, 2);
    Method meth = Util.class.getDeclaredMethod("changedValues", Flowable.class);
    meth.setAccessible(true);
    List<Integer> result = ((Flowable<Integer>) meth
        .invoke(Util.class, flowable)).toList().blockingGet();
    int[] assertResult = new int[]{1, 2, 3, 2};
    assertTrue(assertResult.length == result.size());
    for (int i = 0; i < assertResult.length; i++) {
      assertTrue(result.get(i).equals(assertResult[i]));
    }
  }

}
