package com.dogeland.fastmatch;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by htf on 2021/3/25.
 */
public interface Matcher<R extends Rule, T> {

    void setRules(List<R> rules);

    List<R> match(T matched);

    default void match(T matched, Consumer<List<R>> matchHandler) {
        matchHandler.accept(match(matched));
    }
}
