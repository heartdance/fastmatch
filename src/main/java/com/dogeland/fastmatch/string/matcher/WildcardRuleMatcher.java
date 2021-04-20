package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by htf on 2021/4/20.
 */
public class WildcardRuleMatcher<T extends StringRule> implements StringRuleMatcher<T> {

    protected WildcardCharacterDict<T> dict;

    @Override
    public void addRule(T rule) {
        if (this.dict == null) {
            this.dict = new WildcardCharacterDict<>();
        }
        String compare = rule.getCompare();
        for (int i = 0; i < rule.getCompare().length() - 1; i++) {
            this.dict.next(compare.charAt(i));
        }
        this.dict.last(compare.charAt(compare.length() - 1), rule);
    }

    @Override
    public List<T> match(String matched) {
        if (dict == null || matched == null || matched.length() == 0) {
            return Collections.emptyList();
        }
        List<T> result = new LinkedList<>();
        dict.createSearch(result::addAll).search(matched);
        return result;
    }

    @Override
    public void match(String matched, Consumer<List<T>> matchHandler) {
        if (dict == null || matched == null || matched.length() == 0) {
            return;
        }
        dict.createSearch(matchHandler).search(matched);
    }
}
