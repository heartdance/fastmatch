# fastmatch

一套基于规则、简单、高效的匹配方式


### 字符串匹配

支持相等、前缀、后缀、包含

```java
List<StringRule> rules = new ArrayList<>();
rules.add(new StringRule(StringRule.Type.EQUAL, "i am a test string"));
rules.add(new StringRule(StringRule.Type.START_WITH, "i am"));
rules.add(new StringRule(StringRule.Type.END_WITH, "test string"));
rules.add(new StringRule(StringRule.Type.CONTAIN, "test"));
matcher.setRules(rules);

Matcher<StringRule, String> matcher = new StringMatcher<>();

// 命中所有规则
List<StringRule> matchResult = matcher.match("i am a test string");
```

### 范围匹配

支持开闭区间，自动切割、合并重叠项

```java
List<RangeRule<Integer>> rules = new ArrayList<>();
rules.add(new RangeRule<>(Cut.eq(10), Cut.lt(30))); // 规则1
rules.add(new RangeRule<>(Cut.gt(20), Cut.eq(40))); // 规则2
matcher.setRules(rules);

Matcher<RangeRule<Integer>, Integer> matcher = new RangeMatcher<>();

// 命中规则1
List<RangeRule<Integer>> matchResult1 = matcher.match(10);
// 命中规则1、规则2
List<RangeRule<Integer>> matchResult2 = matcher.match(25);
// 命中规则2
List<RangeRule<Integer>> matchResult3 = matcher.match(30);
```
