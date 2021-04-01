package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;

/**
 * Created by htf on 2021/3/26.
 */
public class CharacterDict<T extends StringRule> {

    private final Map<Character, CharacterNode<T>> nodeMap = new HashMap<>();

    private Map<Character, CharacterNode<T>> tempNodeMap = nodeMap;

    public void next(char c) {
        CharacterNode<T> containIndex = tempNodeMap.computeIfAbsent(c, k -> new CharacterNode<>());
        if (containIndex.childNodeMap == null) {
            containIndex.childNodeMap = new HashMap<>();
        }
        tempNodeMap = containIndex.childNodeMap;
    }

    public void last(char c, T rule) {
        CharacterNode<T> containIndex = tempNodeMap.computeIfAbsent(c, k -> new CharacterNode<>());
        containIndex.addRule(rule);
        tempNodeMap = nodeMap;
    }

    public Searcher<T> createSearcher() {
        return new Searcher<>(this);
    }

    public static class Searcher<T extends StringRule> {

        private final CharacterDict<T> dict;
        private Map<Character, CharacterNode<T>> tempDict;
        private boolean hasNext = true;

        private Searcher(CharacterDict<T> dict) {
            this.dict = dict;
            reset();
        }

        public void reset() {
            this.tempDict = dict.nodeMap;
            this.hasNext = true;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public List<T> next(char c) {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            CharacterNode<T> node = tempDict.get(c);
            if (node == null) {
                this.hasNext = false;
                return null;
            }
            if (node.childNodeMap == null) {
                this.hasNext = false;
            } else {
                tempDict = node.childNodeMap;
            }
            return node.rules;
        }
    }

    private static class CharacterNode<T extends StringRule> {
        public Map<Character, CharacterNode<T>> childNodeMap;
        public List<T> rules;

        public void addRule(T rule) {
            if (this.rules == null) {
                this.rules = new LinkedList<>();
            }
            this.rules.add(rule);
        }
    }
}
