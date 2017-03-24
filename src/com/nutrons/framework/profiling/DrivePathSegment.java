package com.nutrons.framework.profiling;

import org.apache.tools.ant.types.Path;

public class DrivePathSegment {
  private PathSegment left;
  private PathSegment right;

  public DrivePathSegment(PathSegment left,
      PathSegment right) {
    this.left = left;
    this.right = right;
  }

  public PathSegment getLeft() {
    return left;
  }

  public PathSegment getRight() {
    return right;
  }
}