package com.dogeland.fastmatch.string.matcher;

import com.dogeland.fastmatch.string.StringRule;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by htf on 2021/3/26.
 */
public class WildcardCharacterDict<T extends StringRule> {

    private final CharacterNode<T> firstNode = new CharacterNode<>();

    private CharacterNode<T> tempNode = firstNode;

    public void next(char c) {
        if (c == '*') {
            if (tempNode.asteriskNode == null) {
                tempNode.asteriskNode = new CharacterNode<>();
            }
            tempNode = tempNode.asteriskNode;
        } else if (c == '?') {
            if (tempNode.questionNode == null) {
                tempNode.questionNode = new CharacterNode<>();
            }
            tempNode = tempNode.questionNode;
        } else {
            if (tempNode.childNodeMap == null) {
                tempNode.childNodeMap = new HashMap<>();
            }
            tempNode = tempNode.childNodeMap.computeIfAbsent(c, k -> new CharacterNode<>());
        }
    }

    public void last(char c, T rule) {
        next(c);
        if (tempNode.rules == null) {
            tempNode.rules = new LinkedList<>();
        }
        tempNode.rules.add(rule);
        tempNode = firstNode;
    }

    public WildcardSearcher<T> createSearch(Consumer<List<T>> matchHandler) {
        return new WildcardSearcher<>(firstNode, matchHandler);
    }

    public static class CharacterNode<T extends StringRule> {
        public Map<Character, CharacterNode<T>> childNodeMap;
        public CharacterNode<T> questionNode;
        public CharacterNode<T> asteriskNode;
        public List<T> rules;

        public void addRule(T rule) {
            if (this.rules == null) {
                this.rules = new LinkedList<>();
            }
            this.rules.add(rule);
        }
    }

    public static class WildcardSearcher<T extends StringRule> {
        private final CharacterNode<T> dict;
        private final Consumer<List<T>> matchHandler;

        public WildcardSearcher(CharacterNode<T> dict, Consumer<List<T>> matchHandler) {
            this.dict = dict;
            this.matchHandler = matchHandler;
        }

        private String text;
        private Set<CharacterNode<T>> alreadyAdd;

        public void search(String text) {
            this.text = text;
            alreadyAdd = new HashSet<>();
            search(0, dict);
        }

        private void search(int offset, CharacterNode<T> tempDict) {
            for (int i = offset; i < text.length(); i++) {
                if (tempDict.childNodeMap != null) {
                    CharacterNode<T> node = tempDict.childNodeMap.get(text.charAt(i));
                    if (node != null) {
                        if (node.rules != null && i == text.length() - 1 && alreadyAdd.add(node)) {
                            matchHandler.accept(node.rules);
                        }
                        search(i + 1, node);
                    }
                }

                if (tempDict.questionNode != null) {
                    CharacterNode<T> node = tempDict.questionNode;
                    if (node.rules != null && i == text.length() - 1 && alreadyAdd.add(node)) {
                        matchHandler.accept(node.rules);
                    }
                    search(i + 1, node);
                }

                if (tempDict.asteriskNode != null) {
                    CharacterNode<T> node = tempDict.asteriskNode;
                    if (node.rules != null && alreadyAdd.add(node)) {
                        matchHandler.accept(node.rules);
                    }
                    for (int j = i; j < text.length(); j++) {
                        search(j, node);
                    }
                }
            }
        }
    }
}
