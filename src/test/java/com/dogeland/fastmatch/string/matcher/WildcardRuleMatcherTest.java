package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link WildcardRuleMatcher}
 *
 * Created by htf on 2021/4/20.
 */
public class WildcardRuleMatcherTest {

    private final WildcardRuleMatcher<StringRule> matcher = new WildcardRuleMatcher<>();

    @Test
    public void testNoRule() {
        List<StringRule> result = matcher.match("test");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOneRule() {
        StringRule rule = new StringRule(StringRule.Type.WILDCARD, "the ** is ?");
        matcher.addRule(rule);

        List<StringRule> result = matcher.match("the letter is a");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule, result.get(0));

        result = matcher.match("the  is b");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule, result.get(0));

        result = matcher.match("letter is a");
        Assert.assertEquals(0, result.size());

        result = matcher.match("the letter is ");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOneMoreRule() {
        StringRule rule1 = new StringRule(StringRule.Type.WILDCARD, "the * is ?");
        StringRule rule2 = new StringRule(StringRule.Type.WILDCARD, "the letter is a");
        StringRule rule3 = new StringRule(StringRule.Type.WILDCARD, "*");
        matcher.addRule(rule1);
        matcher.addRule(rule2);
        matcher.addRule(rule3);

        List<StringRule> result = matcher.match("the letter is a");
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(rule2, result.get(0));
        Assert.assertEquals(rule1, result.get(1));
        Assert.assertEquals(rule3, result.get(2));
    }

    @Test
    public void testSameRule() {
        StringRule rule = new StringRule(StringRule.Type.WILDCARD, "test");
        matcher.addRule(rule);
        matcher.addRule(rule);

        List<StringRule> result = matcher.match("test");
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(rule, result.get(0));
        Assert.assertEquals(rule, result.get(1));
    }

}
