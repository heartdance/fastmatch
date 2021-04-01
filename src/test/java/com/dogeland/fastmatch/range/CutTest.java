package com.dogeland.fastmatch.range;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Cut}
 *
 * Created by htf on 2021/3/26.
 */
public class CutTest {

    @Test
    public void testCompareEq() {
        Cut<Integer> cut1 = new Cut<>(1, BoundType.EQ);
        Cut<Integer> cut2 = new Cut<>(1, BoundType.EQ);
        Assert.assertEquals(0, cut1.compareTo(cut2));

        cut1 = new Cut<>(1, BoundType.LT);
        cut2 = new Cut<>(1, BoundType.LT);
        Assert.assertEquals(0, cut1.compareTo(cut2));

        cut1 = new Cut<>(1, BoundType.GT);
        cut2 = new Cut<>(1, BoundType.GT);
        Assert.assertEquals(0, cut1.compareTo(cut2));
    }

    @Test
    public void testCompareDiffEndpoint() {
        Cut<Integer> cut1 = new Cut<>(1, BoundType.GT);
        Cut<Integer> cut2 = new Cut<>(2, BoundType.GT);
        Assert.assertEquals(-1, cut1.compareTo(cut2));

        cut1 = new Cut<>(1, BoundType.EQ);
        cut2 = new Cut<>(2, BoundType.EQ);
        Assert.assertEquals(-1, cut1.compareTo(cut2));

        cut1 = new Cut<>(2, BoundType.GT);
        cut2 = new Cut<>(1, BoundType.GT);
        Assert.assertEquals(1, cut1.compareTo(cut2));

        cut1 = new Cut<>(2, BoundType.EQ);
        cut2 = new Cut<>(1, BoundType.EQ);
        Assert.assertEquals(1, cut1.compareTo(cut2));
    }

    @Test
    public void testCompareDiffBoundType() {
        Cut<Integer> cut1 = new Cut<>(1, BoundType.LT);
        Cut<Integer> cut2 = new Cut<>(1, BoundType.EQ);
        Cut<Integer> cut3 = new Cut<>(1, BoundType.GT);
        Assert.assertEquals(-1, cut1.compareTo(cut2));
        Assert.assertEquals(-1, cut2.compareTo(cut3));
    }

    @Test
    public void testCompareDiffAll() {
        Cut<Integer> cut1 = new Cut<>(2, BoundType.LT);
        Cut<Integer> cut2 = new Cut<>(1, BoundType.EQ);
        Assert.assertEquals(1, cut1.compareTo(cut2));

        cut1 = new Cut<>(2, BoundType.EQ);
        cut2 = new Cut<>(1, BoundType.LT);
        Assert.assertEquals(1, cut1.compareTo(cut2));
    }
}
