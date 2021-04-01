package com.dogeland.fastmatch.range;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by htf on 2021/3/26.
 */
public class RangeDict<T extends Comparable<T>, R extends RangeRule<T>> {

    private final List<CompiledRange<T, R>> compiledRanges;

    public RangeDict(List<R> rules) {
        this.compiledRanges = compileRules(rules);
    }

    private List<CompiledRange<T, R>> compileRules(List<R> rules) {
        List<CompiledRange<T, R>> compiledRanges = new ArrayList<>();
        for (R rule : rules) {
            RangeRule<T> unmerge = rule;
            for (int i = searchStartIndex(compiledRanges, unmerge); i < compiledRanges.size(); i++) {
                CompiledRange<T, R> range = compiledRanges.get(i);

                // 新规则与当前规则无交集
                if (range.lower().compareTo(unmerge.upper()) > 0) {
                    //   -
                    // -
                    compiledRanges.add(i, new CompiledRange<>(unmerge.lower(), unmerge.upper(), rule));
                    unmerge = null;
                    break;
                }
                if (range.upper().compareTo(unmerge.lower()) < 0) {
                    // -
                    //   -
                    continue;
                }

                int lowerCompare = range.lower().compareTo(unmerge.lower());
                int upperCompare = range.upper().compareTo(unmerge.upper());
                if (lowerCompare == 0) {
                    if (upperCompare == 0) {
                        range.addRule(rule);
                        unmerge = null;
                        break;
                    } else if (upperCompare > 0) {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(unmerge.lower(), unmerge.upper());
                        compiledRange.addRules(range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.add(i++, compiledRange);
                        compiledRanges.set(i, new CompiledRange<>(unmerge.upper().right(), range.upper(), range.rules()));
                        unmerge = null;
                        break;
                    } else {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(unmerge.lower(), range.upper(), range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.set(i, compiledRange);
                        unmerge = new RangeRule<>(range.upper().right(), unmerge.upper());
                    }
                } else if (lowerCompare > 0) {
                    compiledRanges.add(i++, new CompiledRange<>(unmerge.lower(), range.lower().left(), rule));
                    if (upperCompare == 0) {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(range.lower(), range.upper(), range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.set(i, compiledRange);
                        unmerge = null;
                        break;
                    } else if (upperCompare > 0) {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(range.lower(), unmerge.upper());
                        compiledRange.addRules(range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.add(i++, compiledRange);
                        compiledRanges.set(i, new CompiledRange<>(unmerge.upper().right(), range.upper(), range.rules()));
                        unmerge = null;
                        break;
                    } else {
                        range.addRule(rule);
                        unmerge = new RangeRule<>(range.upper().right(), unmerge.upper());
                    }
                } else {
                    compiledRanges.add(i++, new CompiledRange<>(range.lower(), unmerge.lower().left(), range.rules()));
                    if (upperCompare == 0) {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(unmerge.lower(), unmerge.upper());
                        compiledRange.addRules(range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.set(i, compiledRange);
                        unmerge = null;
                        break;
                    } else if (upperCompare > 0) {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(unmerge.lower(), unmerge.upper());
                        compiledRange.addRules(range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.add(i++, compiledRange);
                        compiledRange = new CompiledRange<>(unmerge.upper().right(), range.upper());
                        compiledRange.addRules(range.rules);
                        compiledRanges.set(i, compiledRange);
                        unmerge = null;
                        break;
                    } else {
                        CompiledRange<T, R> compiledRange = new CompiledRange<>(unmerge.lower(), range.upper());
                        compiledRange.addRules(range.rules());
                        compiledRange.addRule(rule);
                        compiledRanges.set(i, compiledRange);
                        unmerge = new RangeRule<>(range.upper().right(), unmerge.upper());
                    }
                }
            }

            // 将规则的未合并部分添加到尾部
            if (unmerge != null) {
                compiledRanges.add(new CompiledRange<>(unmerge.lower(), unmerge.upper(), rule));
            }
        }
        return compiledRanges;
    }

    private int searchStartIndex(List<CompiledRange<T, R>> compiledRanges, RangeRule<T> rule) {
        int startIndex = 0;
        int searchDir = 1;
        int tempIndex = 0;
        for (int step = compiledRanges.size() / 2; step > 0; step /= 2) {
            tempIndex = tempIndex + searchDir * step;
            CompiledRange<T, R> range = compiledRanges.get(tempIndex);
            int compare = range.upper().compareTo(rule.lower());
            if (compare < 0) {
                searchDir = 1;
                startIndex = tempIndex + 1;
            } else if (compare > 0) {
                searchDir = -1;
            } else {
                startIndex = tempIndex;
                break;
            }
        }
        return startIndex;
    }

    public List<CompiledRange<T, R>> getCompiledRanges() {
        return compiledRanges;
    }

    public static class CompiledRange<T extends Comparable<T>, R extends RangeRule<T>> extends RangeRule<T> {

        private List<R> rules;

        public CompiledRange(Cut<T> lower, Cut<T> upper) {
            super(lower, upper);
        }

        public CompiledRange(Cut<T> lower, Cut<T> upper, R rule) {
            super(lower, upper);
            this.addRule(rule);
        }

        public CompiledRange(Cut<T> lower, Cut<T> upper, List<R> rules) {
            super(lower, upper);
            this.setRules(rules);
        }

        public void addRule(R rule) {
            if (this.rules == null) {
                this.rules = new LinkedList<>();
            }
            this.rules.add(rule);
        }

        public void addRules(List<R> rules) {
            if (this.rules == null) {
                this.rules = new LinkedList<>();
            }
            this.rules.addAll(rules);
        }

        public List<R> rules() {
            return rules;
        }

        public void setRules(List<R> rules) {
            this.rules = rules;
        }
    }
}
