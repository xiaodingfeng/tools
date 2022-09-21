package org.xiaobai.constants;

import lombok.Data;

/**
 * 缓存key
 * @author xdf
 */
public enum KeyEnum {
    ONE_WORLD("cache:one_word", "每日一句")
    ;

    private final String value;
    private final String label;

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    KeyEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
