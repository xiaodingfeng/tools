package org.xiaobai.im.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聊天室 消息类型
 */
@AllArgsConstructor
public enum ChatTypeEnum {
    CHAT_MSG_ERROR("90001", "消息接收格式错误"),
    CHAT_ONLINE_COUNT("10001", "在线人数"),
    CHAT_USER_ONLINE("10002", "用户上线"),
    CHAT_USER_DOWN_LINE("10003", "用户下线"),
    CHAT_USER_USER("10004", "user-user"),
    CHAT_USER_GROUP("10005", "user-group"),
    SOCKET_HEART("20001", "心跳检测");

    @Getter
    @EnumValue
    private final String type;

    @Getter
    @JsonValue
    private final String message;
}
