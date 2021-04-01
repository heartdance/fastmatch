package com.dogeland.fastmatch.range;

import com.dogeland.fastmatch.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link RangeMatcher}
 *
 * Created by htf on 2021/3/29.
 */
public class RangeMatcherTest {

    @Test
    public void testMatch() {
        Matcher<RangeRule<Integer>, Integer> matcher = new RangeMatcher<>();

        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.lt(20)));
        rules.add(new RangeRule<>(Cut.gt(30), Cut.eq(40)));
        matcher.setRules(rules);

        Assert.assertEquals(0, matcher.match(9).size());
        Assert.assertEquals(1, matcher.match(10).size());
        Assert.assertEquals(rules.get(0), matcher.match(10).get(0));
        Assert.assertEquals(1, matcher.match(15).size());
        Assert.assertEquals(rules.get(0), matcher.match(15).get(0));
        Assert.assertEquals(0, matcher.match(20).size());

        Assert.assertEquals(0, matcher.match(30).size());
        Assert.assertEquals(1, matcher.match(40).size());
        Assert.assertEquals(rules.get(1), matcher.match(40).get(0));
        Assert.assertEquals(0, matcher.match(41).size());
    }
}
