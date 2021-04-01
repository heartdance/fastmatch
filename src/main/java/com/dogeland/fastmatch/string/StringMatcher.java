package com.dogeland.fastmatch.string;

import com.dogeland.fastmatch.Matcher;
import com.dogeland.fastmatch.string.matcher.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by htf on 2021/3/25.
 */
public class StringMatcher<T extends StringRule> implements Matcher<T, String> {

    private List<StringRuleMatcher<T>> matchers;

    @Override
    public void setRules(List<T> rules) {
        List<StringRuleMatcher<T>> matchers = new ArrayList<>(Arrays.asList(null, null, null, null));
        for (T rule : rules) {
            StringRule.Type type = rule.getType();
            StringRuleMatcher<T> matcher = matchers.get(type.code());
            if (matcher == null) {
                matchers.set(type.code(), createMatcherByType(type));
                matcher = matchers.get(type.code());
            }
            matcher.addRule(rule);
        }
        for (int i = matchers.size() - 1; i >= 0; i--) {
            if (matchers.get(i) == null) {
                matchers.remove(i);
            }
        }
        this.matchers = matchers;
    }

    @Override
    public List<T> match(String matched) {
        List<T> result = new LinkedList<>();
        for (StringRuleMatcher<T> stringRuleMatcher : matchers) {
            result.addAll(stringRuleMatcher.match(matched));
        }
        return result;
    }

    private StringRuleMatcher<T> createMatcherByType(StringRule.Type type) {
        switch (type) {
            case EQUAL:
                return new EqualRuleMatcher<>();
            case CONTAIN:
                return new ContainRuleMatcher<>();
            case START_WITH:
                return new StartWithRuleMatcher<>();
            case END_WITH:
                return new EndWithRuleMatcher<>();
        }
        throw new IllegalArgumentException();
    }
}
