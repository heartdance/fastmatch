package com.dogeland.fastmatch.string;

import com.dogeland.fastmatch.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link StringMatcher}
 *
 * Created by htf on 2021/3/25.
 */
public class StringMatcherTest {

    @Test
    public void testMatch() {
        Matcher<StringRule, String> matcher = new StringMatcher<>();
        List<StringRule> rules = new ArrayList<>();
        rules.add(new StringRule(StringRule.Type.EQUAL, "i am a test string"));
        rules.add(new StringRule(StringRule.Type.START_WITH, "i am"));
        rules.add(new StringRule(StringRule.Type.END_WITH, "test string"));
        rules.add(new StringRule(StringRule.Type.CONTAIN, "test"));
        matcher.setRules(rules);

        List<StringRule> matchResult = matcher.match("i am a test string");
        Assert.assertEquals(4, matchResult.size());
        containsAll(matchResult, rules);

        matchResult = matcher.match("what are you");
        Assert.assertEquals(0, matchResult.size());

        matchResult = matcher.match("i am a test string too");
        Assert.assertEquals(2, matchResult.size());
        containsAll(matchResult, rules.get(1), rules.get(3));
    }

    private static <T> void containsAll(List<T> source, List<T> contained) {
        for (T t : contained) {
            Assert.assertTrue(source.contains(t));
        }
    }

    @SafeVarargs
    private static <T> void containsAll(List<T> source, T... contained) {
        for (T t : contained) {
            Assert.assertTrue(source.contains(t));
        }
    }
}
