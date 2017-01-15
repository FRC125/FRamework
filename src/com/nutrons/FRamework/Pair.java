package com.nutrons.FRamework;

public class Pair<L, R> {
    private L l;
    private R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L left() {
        return this.l;
    }

    public R right() {
        return this.r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        sb.append(this.l);
        sb.append(", ");
        sb.append(this.r);
        sb.append(" )");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Pair && ((Pair) o).left().equals(this.l) && ((Pair) o).right().equals(this.r);
    }
}
