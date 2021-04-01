package com.dogeland.fastmatch.range;

import com.dogeland.fastmatch.Rule;

import java.util.Objects;

/**
 * Created by htf on 2021/3/26.
 */
public class RangeRule<T extends Comparable<T>> implements Rule {

    private final Cut<T> lower;
    private final Cut<T> upper;

    public RangeRule(Cut<T> lower, Cut<T> upper) {
        if (lower == null || upper == null) {
            throw new NullPointerException();
        }
        if (lower.endpoint() == BoundType.LT) {
            throw new IllegalArgumentException("rule lower bound type cannot be lt");
        }
        if (upper.endpoint() == BoundType.GT) {
            throw new IllegalArgumentException("rule upper bound type cannot be gt");
        }
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException("rule lower cannot greater than upper");
        }
        this.lower = lower;
        this.upper = upper;
    }

    public Cut<T> lower() {
        return lower;
    }

    public Cut<T> upper() {
        return upper;
    }

    /**
     * @return cut 位置
     * -1: cut在左侧范围外
     * 0: cut在范围内
     * 1: cut在右侧范围外
     */
    public int position(Cut<T> cut) {
        if (this.lower.compareTo(cut) > 0) {
            return -1;
        }
        if (this.upper.compareTo(cut) < 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeRule<?> rangeRule = (RangeRule<?>) o;
        return lower.equals(rangeRule.lower) &&
                upper.equals(rangeRule.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    @Override
    public String toString() {
        return "RangeRule{" +
                "lower=" + lower +
                ", upper=" + upper +
                '}';
    }
}
