package com.dogeland.fastmatch.string;

import com.dogeland.fastmatch.Rule;

/**
 * Created by htf on 2021/3/25.
 */
public class StringRule implements Rule {

    private Type type;
    private String compare;

    public StringRule() {
    }

    public StringRule(Type type, String compare) {
        this.type = type;
        this.compare = compare;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public enum Type {
        EQUAL(0), START_WITH(1), END_WITH(2), CONTAIN(3), WILDCARD(4);

        private final int code;

        Type(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }

        public static Type code(int code) {
            switch (code) {
                case 0: return EQUAL;
                case 1: return START_WITH;
                case 2: return END_WITH;
                case 3: return CONTAIN;
                case 4: return WILDCARD;
            }
            throw new IllegalArgumentException();
        }
    }
}
