package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by htf on 2021/3/25.
 */
public interface StringRuleMatcher<T extends StringRule> {

    void addRule(T rule);

    List<T> match(String matched);

    default void match(String matched, Consumer<List<T>> matchHandler) {
        matchHandler.accept(match(matched));
    }
}
