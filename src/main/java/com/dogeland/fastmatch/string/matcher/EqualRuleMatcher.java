package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;

/**
 * Created by htf on 2021/3/25.
 */
public class EqualRuleMatcher<T extends StringRule> implements StringRuleMatcher<T> {

    private Map<String, List<T>> equalRules;

    @Override
    public void addRule(T rule) {
        if (equalRules == null) {
            equalRules = new HashMap<>();
        }
        equalRules.computeIfAbsent(rule.getCompare(), k -> new LinkedList<>()).add(rule);
    }

    @Override
    public List<T> match(String matched) {
        if (equalRules == null) {
            return Collections.emptyList();
        }
        return equalRules.getOrDefault(matched, Collections.emptyList());
    }
}
