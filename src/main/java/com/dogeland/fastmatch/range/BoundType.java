package com.dogeland.fastmatch.range;

/**
 * Created by htf on 2021/3/26.
 */
public enum BoundType {
    GT(1), LT(-1), EQ(0);

    private final int pos;

    BoundType(int pos) {
        this.pos = pos;
    }

    public int pos() {
        return pos;
    }

    public static BoundType pos(int pos) {
        switch (pos) {
            case 1: return GT;
            case -1: return LT;
            case 0: return EQ;
        }
        throw new IllegalArgumentException();
    }
}
