package com.nutrons.framework.profiling;

import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class reader {
  public static Flowable<DrivePathSegment> read(String serialized){
    StringTokenizer tokenizer = new StringTokenizer(serialized, "\n");

    String name = tokenizer.nextToken();
    int num_elements = Integer.parseInt(tokenizer.nextToken());

    List<PathSegment> left = new ArrayList<>();
    List<DrivePathSegment> trajectories = new ArrayList<>();

    for (int i = 0; i < num_elements; i++) {

      StringTokenizer line_tokenizer = new StringTokenizer(
          tokenizer.nextToken(), " ");
      double vel = Double.parseDouble(line_tokenizer.nextToken());
      double head = Double.parseDouble(line_tokenizer.nextToken());

      PathSegment segment = new PathSegment(vel, head);

      left.add(segment);
    }
    for (int i = 0; i < num_elements; i++) {

      StringTokenizer line_tokenizer = new StringTokenizer(
          tokenizer.nextToken(), " ");
      double vel = Double.parseDouble(line_tokenizer.nextToken());
      double head = Double.parseDouble(line_tokenizer.nextToken());


      trajectories.add(new DrivePathSegment(left.get(i), new PathSegment(vel, head)));
    }

    return Flowable.fromIterable(trajectories);
  }

}


