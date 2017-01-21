package com.nutrons.FRamework.util;

public class Pair<L, R> {
  private L left;
  private R right;

  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public L left() {
    return this.left;
  }

  public R right() {
    return this.right;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("( ");
    sb.append(this.left);
    sb.append(", ");
    sb.append(this.right);
    sb.append(" )");
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Pair
        && ((Pair) obj).left().equals(this.left)
        && ((Pair) obj).right().equals(this.right);
  }
}
