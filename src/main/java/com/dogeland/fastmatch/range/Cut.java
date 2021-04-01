package com.dogeland.fastmatch.range;

import java.util.Objects;

/**
 * Created by htf on 2021/3/26.
 */
public class Cut<T extends Comparable<T>> implements Comparable<Cut<T>> {

    private final T endpoint;
    private final BoundType type;

    public Cut(T endpoint, BoundType type) {
        if (endpoint == null || type == null) {
            throw new NullPointerException();
        }
        this.endpoint = endpoint;
        this.type = type;
    }

    public static <T extends Comparable<T>> Cut<T> gt(T endpoint) {
        return new Cut<>(endpoint, BoundType.GT);
    }

    public static <T extends Comparable<T>> Cut<T> lt(T endpoint) {
        return new Cut<>(endpoint, BoundType.LT);
    }

    public static <T extends Comparable<T>> Cut<T> eq(T endpoint) {
        return new Cut<>(endpoint, BoundType.EQ);
    }

    public T endpoint() {
        return endpoint;
    }

    public BoundType type() {
        return type;
    }

    public Cut<T> gt() {
        return new Cut<>(this.endpoint, BoundType.GT);
    }

    public Cut<T> lt() {
        return new Cut<>(this.endpoint, BoundType.LT);
    }

    public Cut<T> eq() {
        return new Cut<>(this.endpoint, BoundType.EQ);
    }

    public Cut<T> right() {
        return new Cut<>(this.endpoint, BoundType.pos(this.type.pos() + 1));
    }

    public Cut<T> left() {
        return new Cut<>(this.endpoint, BoundType.pos(this.type.pos() - 1));
    }

    public Cut<T> endpoint(T endpoint) {
        return new Cut<>(endpoint, this.type);
    }

    @Override
    public int compareTo(Cut<T> o) {
        int compare = endpoint.compareTo(o.endpoint);
        if (compare == 0) {
            if (type == o.type) {
                return compare;
            }
            if (type.pos() > o.type.pos()) {
                return 1;
            }
            return -1;
        }
        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cut<?> cut = (Cut<?>) o;
        return endpoint.equals(cut.endpoint) &&
                type == cut.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, type);
    }

    @Override
    public String toString() {
        return "Cut{" +
                "endpoint=" + endpoint +
                ", type=" + type +
                '}';
    }
}
