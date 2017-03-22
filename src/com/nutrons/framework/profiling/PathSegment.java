package com.nutrons.framework.profiling;

/**
 * Created by Josh on 3/21/2017.
 */
public class PathSegment {
  private double velocity;
  private double heading;

  public PathSegment(double velocity, double heading) {
    this.heading = heading;
    this.velocity = velocity;
  }


  public double getVelocity() {
    return velocity;
  }

  public double getHeading() {
    return heading;
  }
}
