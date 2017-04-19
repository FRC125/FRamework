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

  public static Flowable<Pair<Double[], Double[]>> read(Reader reader) {
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

      Flowable<Double[]> dataParsed = data.map(x -> {
        String[] split = x.split(" ");
        Double[] mata = new Double[6];

        for(int i = 0; i < mata.length; i++){
          mata[i] = Double.parseDouble(split[i]);
        }

        if(split.length != 6){
          System.out.println("the parsed data was not of length 6, it was of length: " + split.length);
        }

        return mata;
      });

      data = data.replay().autoConnect();
      dataParsed = dataParsed.replay().autoConnect();

      return Flowable.zip(dataParsed.take(num_elements), dataParsed.skip(num_elements), Pair::new);
    } catch (IOException e) {
      e.printStackTrace();
      return Flowable.empty();
    }
  }

  public static Flowable<Pair<Double[], Double[]>> read(File file) {
    //return two trajectory targets flowables
    try {
      FileReader fr = new FileReader(file);
      return read(fr);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return Flowable.empty();
    }
  }
}


