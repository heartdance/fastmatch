package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by htf on 2021/3/25.
 */
public class ContainRuleMatcher<T extends StringRule> implements StringRuleMatcher<T> {

    protected CharacterDict<T> dict;

    @Override
    public void addRule(T rule) {
        if (this.dict == null) {
            this.dict = new CharacterDict<>();
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
        Set<String> added = new HashSet<>();
        CharacterDict.Searcher<T> searcher = dict.createSearcher();
        for (int i = 0; i < matched.length(); i++) {
            for (int j = i; j < matched.length() && searcher.hasNext(); j++) {
                char c = matched.charAt(j);
                List<T> searched = searcher.next(c);
                if (searched != null && added.add(matched.substring(i, j + 1))) {
                    result.addAll(searched);
                }
            }
            searcher.reset();
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
        for (int i = 0; i < matched.length(); i++) {
            for (int j = i; j < matched.length() && searcher.hasNext(); j++) {
                char c = matched.charAt(j);
                List<T> searched = searcher.next(c);
                if (searched != null && added.add(matched.substring(i, j + 1))) {
                    matchHandler.accept(searched);
                }
            }
            searcher.reset();
        }
    }
}
