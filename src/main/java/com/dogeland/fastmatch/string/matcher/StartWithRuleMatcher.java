package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;

/**
 * Created by htf on 2021/3/25.
 */
public class StartWithRuleMatcher<T extends StringRule> extends ContainRuleMatcher<T> {

    @Override
    public List<T> match(String matched) {
        if (dict == null || matched == null || matched.length() == 0) {
            return Collections.emptyList();
        }
        List<T> result = new LinkedList<>();
        Set<String> added = new HashSet<>();
        CharacterDict.Searcher<T> searcher = dict.createSearcher();
        for (int i = 0; i < matched.length() && searcher.hasNext(); i++) {
            char c = matched.charAt(i);
            List<T> searched = searcher.next(c);
            if (searched != null && added.add(matched.substring(0, i + 1))) {
                result.addAll(searched);
            }
        }
        return result;
    }
}
