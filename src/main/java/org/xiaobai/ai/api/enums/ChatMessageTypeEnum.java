package org.xiaobai.ai.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聊天消息类型枚举
 */
@AllArgsConstructor
public enum ChatMessageTypeEnum {

    /**
     * 问题
     */
    QUESTION(1),

    /**
     * 回答
     */
    ANSWER(2);

    @Getter
    @EnumValue
    private final Integer code;
}
