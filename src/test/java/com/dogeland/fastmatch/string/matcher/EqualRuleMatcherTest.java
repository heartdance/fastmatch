package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link EqualRuleMatcher}
 *
 * Created by htf on 2021/3/25.
 */
public class EqualRuleMatcherTest {

    private final EqualRuleMatcher<StringRule> matcher = new EqualRuleMatcher<>();

    @Test
    public void testNoRule() {
        List<StringRule> result = matcher.match("test");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOneRule() {
        StringRule rule = new StringRule(StringRule.Type.EQUAL, "test1");
        matcher.addRule(rule);

        List<StringRule> result = matcher.match("test1");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule, result.get(0));
    }

    @Test
    public void testOneMoreRule() {
        StringRule rule1 = new StringRule(StringRule.Type.EQUAL, "test1");
        StringRule rule2 = new StringRule(StringRule.Type.EQUAL, "test2");
        matcher.addRule(rule1);
        matcher.addRule(rule2);

        List<StringRule> result = matcher.match("test1");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule1, result.get(0));

        result = matcher.match("test2");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule2, result.get(0));
    }

    @Test
    public void testSameRule() {
        StringRule rule = new StringRule(StringRule.Type.EQUAL, "test1");
        matcher.addRule(rule);
        matcher.addRule(rule);

        List<StringRule> result = matcher.match("test1");
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(rule, result.get(0));
        Assert.assertEquals(rule, result.get(1));
    }
}
