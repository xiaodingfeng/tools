package org.xiaobai.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ValidEnum
 * @Description TODO
 * @Author dingfeng.xiao
 * @Date 2023/7/10 13:12
 * @Version 1.0
 */
@AllArgsConstructor
public enum ValidEnum {
    /**
     * DAY
     */
    VALID(1, "有效"),
    /**
     * WEEK
     */
    IN_VALID(0, "无效");
    @Getter
    @EnumValue
    private final int code;

    @Getter
    @JsonValue
    private final String message;
}
