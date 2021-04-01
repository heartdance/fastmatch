package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link EndWithRuleMatcher}
 *
 * Created by htf on 2021/3/26.
 */
public class EndWithRuleMatcherTest {

    private final EndWithRuleMatcher<StringRule> matcher = new EndWithRuleMatcher<>();

    @Test
    public void testNoRule() {
        List<StringRule> result = matcher.match("test");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOneRule() {
        StringRule rule = new StringRule(StringRule.Type.END_WITH, "test");
        matcher.addRule(rule);

        List<StringRule> result = matcher.match("test");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule, result.get(0));

        result = matcher.match("1test");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule, result.get(0));

        result = matcher.match("test1");
        Assert.assertEquals(0, result.size());

        result = matcher.match("tes");
        Assert.assertEquals(0, result.size());

        result = matcher.match("est");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOneMoreRule() {
        StringRule rule1 = new StringRule(StringRule.Type.END_WITH, "test");
        StringRule rule2 = new StringRule(StringRule.Type.END_WITH, "1test");
        StringRule rule3 = new StringRule(StringRule.Type.END_WITH, "2test");
        matcher.addRule(rule1);
        matcher.addRule(rule2);
        matcher.addRule(rule3);

        List<StringRule> result = matcher.match("test");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(rule1, result.get(0));

        result = matcher.match("1test");
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(rule1));
        Assert.assertTrue(result.contains(rule2));

        result = matcher.match("tes");
        Assert.assertEquals(0, result.size());

        result = matcher.match("est");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testSameRule() {
        StringRule rule = new StringRule(StringRule.Type.END_WITH, "test");
        matcher.addRule(rule);
        matcher.addRule(rule);

        List<StringRule> result = matcher.match("pre test");
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(rule, result.get(0));
        Assert.assertEquals(rule, result.get(1));
    }
}
