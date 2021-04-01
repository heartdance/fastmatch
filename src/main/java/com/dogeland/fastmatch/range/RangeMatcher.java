package com.dogeland.fastmatch.range;

import com.dogeland.fastmatch.Matcher;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by htf on 2021/3/26.
 */
public class RangeMatcher<T extends Comparable<T>, R extends RangeRule<T>> implements Matcher<R, T> {

    private TreeMap<Cut<T>, RangeDict.CompiledRange<T, R>> dict = new TreeMap<>();

    @Override
    public void setRules(List<R> rules) {
        TreeMap<Cut<T>, RangeDict.CompiledRange<T, R>> map = new TreeMap<>();
        RangeDict<T, R> dict = new RangeDict<>(rules);
        List<RangeDict.CompiledRange<T, R>> compiledRanges = dict.getCompiledRanges();
        for (RangeDict.CompiledRange<T, R> range : compiledRanges) {
            map.put(range.lower(), range);
        }
        this.dict = map;
    }

    @Override
    public List<R> match(T matched) {
        Cut<T> matchedCut = Cut.eq(matched);
        Map.Entry<Cut<T>, RangeDict.CompiledRange<T, R>> entry = dict.floorEntry(matchedCut);
        if (entry != null) {
            RangeDict.CompiledRange<T, R> value = entry.getValue();
            if (value.upper().compareTo(matchedCut) >= 0) {
                return value.rules();
            }
        }
        return Collections.emptyList();
    }
}
