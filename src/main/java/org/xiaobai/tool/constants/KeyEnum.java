package org.xiaobai.tool.constants;

/**
 * 缓存key
 *
 * @author xdf
 */
public enum KeyEnum {
    ONE_WORLD("cache:one_word", "每日一句"),
    LOGIN_TOKEN("cache:login_token", "登录token");

    private final String value;
    private final String label;

    KeyEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
