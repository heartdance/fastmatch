package com.dogeland.fastmatch;

import java.util.List;

/**
 * Created by htf on 2021/3/25.
 */
public interface Matcher<R extends Rule, T> {

    void setRules(List<R> rules);

    List<R> match(T matched);
}
