package com.nutrons.framework.profiling;

import com.nutrons.framework.util.Pair;
import io.reactivex.Flowable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class MotionDeserialzer {

  public static Flowable<Pair<Double, Double>> read(Reader reader) {
    try {
      BufferedReader br = new BufferedReader(reader);
      String name = br.readLine();
      int num_elements = Integer.parseInt(br.readLine().trim());
      Flowable<String> data = Flowable.generate(() -> br, (b, e) -> {
        String line = br.readLine().trim();
        if (line == null) {
          e.onComplete();
        } else {
          e.onNext(line);
        }
        return br;
      });
      data = data.replay().autoConnect();
      return Flowable.zip(data.take(num_elements), data.skip(num_elements), (l, r) -> {
        double velocityL = Double.parseDouble(l.substring(0, l.indexOf(" ")));
        double velocityR = Double.parseDouble(r.substring(0, r.indexOf(" ")));
        return new Pair<>(velocityL, velocityR);
      });
    } catch (IOException e) {
      e.printStackTrace();
      return Flowable.empty();
    }
  }

  public static Flowable<Pair<Double, Double>> read(File file) {
    try {
      FileReader fr = new FileReader(file);
      return read(fr);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return Flowable.empty();
    }
  }

}


