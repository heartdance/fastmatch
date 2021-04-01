package com.dogeland.fastmatch.range.matcher;

import com.dogeland.fastmatch.range.Cut;
import com.dogeland.fastmatch.range.RangeDict;
import com.dogeland.fastmatch.range.RangeRule;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link RangeDict}
 *
 * Created by htf on 2021/3/26.
 */
public class RangeDictTest {

    @Test
    public void testNoRule() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();
        Assert.assertEquals(0, ranges.size());
    }

    @Test
    public void testSameRangeRule() {
        RangeRule<Integer> rule1 = new RangeRule<>(Cut.eq(10), Cut.lt(20));
        RangeRule<Integer> rule2 = new RangeRule<>(Cut.eq(10), Cut.lt(20));
        List<RangeRule<Integer>> rules = new ArrayList<>(Arrays.asList(rule1, rule2));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(1, ranges.size());
        Assert.assertEquals(Cut.eq(10), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(20), ranges.get(0).upper());
        Assert.assertEquals(2, ranges.get(0).rules().size());
        Assert.assertEquals(rule1, ranges.get(0).rules().get(0));
        Assert.assertEquals(rule2, ranges.get(0).rules().get(1));
    }

    @Test
    public void testSamePointRule() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(10)));
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(10)));
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(10)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(1, ranges.size());

        Assert.assertEquals(Cut.eq(10), ranges.get(0).lower());
        Assert.assertEquals(Cut.eq(10), ranges.get(0).upper());
        Assert.assertEquals(3, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(0).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(1));
        Assert.assertEquals(rules.get(2), ranges.get(0).rules().get(2));
    }

    @Test
    public void testRuleOnLeftAndNotOverlap() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        rules.add(new RangeRule<>(Cut.eq(5), Cut.lt(10)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(2, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(1).upper());
        Assert.assertEquals(1, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
    }

    @Test
    public void testRuleOnLeftAndOverlapPoint() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        rules.add(new RangeRule<>(Cut.eq(5), Cut.eq(10)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(3, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(10), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));

        Assert.assertEquals(Cut.gt(10), ranges.get(2).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(2).upper());
        Assert.assertEquals(1, ranges.get(2).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(2).rules().get(0));
    }

    @Test
    public void testRuleOnLeftAndOverlapRange() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        rules.add(new RangeRule<>(Cut.eq(5), Cut.eq(15)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(3, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(15), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));

        Assert.assertEquals(Cut.gt(15), ranges.get(2).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(2).upper());
        Assert.assertEquals(1, ranges.get(2).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(2).rules().get(0));
    }

    @Test
    public void testRuleOnLeftAndOverlapAll() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        rules.add(new RangeRule<>(Cut.eq(5), Cut.eq(20)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(2, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));
    }

    @Test
    public void testRuleOnRightAndNotOverlap() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(5), Cut.lt(10)));
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(2, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(1).upper());
        Assert.assertEquals(1, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(0));
    }

    @Test
    public void testRuleOnRightAndOverlapPoint() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(5), Cut.eq(10)));
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(3, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(10), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));

        Assert.assertEquals(Cut.gt(10), ranges.get(2).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(2).upper());
        Assert.assertEquals(1, ranges.get(2).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(2).rules().get(0));
    }

    @Test
    public void testRuleOnRightAndOverlapRange() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(5), Cut.eq(15)));
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(3, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(15), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));

        Assert.assertEquals(Cut.gt(15), ranges.get(2).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(2).upper());
        Assert.assertEquals(1, ranges.get(2).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(2).rules().get(0));
    }

    @Test
    public void testRuleOnRightAndOverlapAll() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(20)));
        rules.add(new RangeRule<>(Cut.eq(5), Cut.eq(20)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(2, ranges.size());

        Assert.assertEquals(Cut.eq(5), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(10), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(10), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(20), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));
    }

    @Test
    public void testRuleContained() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(40)));
        rules.add(new RangeRule<>(Cut.eq(20), Cut.eq(30)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(3, ranges.size());

        Assert.assertEquals(Cut.eq(10), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(20), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(20), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(30), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));

        Assert.assertEquals(Cut.gt(30), ranges.get(2).lower());
        Assert.assertEquals(Cut.eq(40), ranges.get(2).upper());
        Assert.assertEquals(1, ranges.get(2).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(2).rules().get(0));
    }

    @Test
    public void testRuleContain() {
        List<RangeRule<Integer>> rules = new ArrayList<>();
        rules.add(new RangeRule<>(Cut.eq(20), Cut.eq(30)));
        rules.add(new RangeRule<>(Cut.eq(10), Cut.eq(40)));
        RangeDict<Integer, RangeRule<Integer>> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<Integer, RangeRule<Integer>>> ranges = dict.getCompiledRanges();

        Assert.assertEquals(3, ranges.size());

        Assert.assertEquals(Cut.eq(10), ranges.get(0).lower());
        Assert.assertEquals(Cut.lt(20), ranges.get(0).upper());
        Assert.assertEquals(1, ranges.get(0).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(0).rules().get(0));

        Assert.assertEquals(Cut.eq(20), ranges.get(1).lower());
        Assert.assertEquals(Cut.eq(30), ranges.get(1).upper());
        Assert.assertEquals(2, ranges.get(1).rules().size());
        Assert.assertEquals(rules.get(0), ranges.get(1).rules().get(0));
        Assert.assertEquals(rules.get(1), ranges.get(1).rules().get(1));

        Assert.assertEquals(Cut.gt(30), ranges.get(2).lower());
        Assert.assertEquals(Cut.eq(40), ranges.get(2).upper());
        Assert.assertEquals(1, ranges.get(2).rules().size());
        Assert.assertEquals(rules.get(1), ranges.get(2).rules().get(0));
    }

}
