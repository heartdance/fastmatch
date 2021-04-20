package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by htf on 2021/3/25.
 */
public class EndWithRuleMatcher<T extends StringRule> implements StringRuleMatcher<T> {

    protected CharacterDict<T> dict;

    @Override
    public void addRule(T rule) {
        if (this.dict == null) {
            this.dict = new CharacterDict<>();
        }
        String compare = rule.getCompare();
        for (int i = rule.getCompare().length() - 1; i > 0; i--) {
            this.dict.next(compare.charAt(i));
        }
        this.dict.last(compare.charAt(0), rule);
    }

    @Override
    public List<T> match(String matched) {
        if (dict == null || matched == null || matched.length() == 0) {
            return Collections.emptyList();
        }
        List<T> result = new LinkedList<>();
        Set<String> added = new HashSet<>();
        CharacterDict.Searcher<T> searcher = dict.createSearcher();
        for (int i = matched.length() - 1; i >= 0 && searcher.hasNext(); i--) {
            char c = matched.charAt(i);
            List<T> searched = searcher.next(c);
            if (searched != null && added.add(matched.substring(i))) {
                result.addAll(searched);
            }
        }
        return result;
    }

    @Override
    public void match(String matched, Consumer<List<T>> matchHandler) {
        if (dict == null || matched == null || matched.length() == 0) {
            return;
        }
        Set<String> added = new HashSet<>();
        CharacterDict.Searcher<T> searcher = dict.createSearcher();
        for (int i = matched.length() - 1; i >= 0 && searcher.hasNext(); i--) {
            char c = matched.charAt(i);
            List<T> searched = searcher.next(c);
            if (searched != null && added.add(matched.substring(i))) {
                matchHandler.accept(searched);
            }
        }
    }
}
