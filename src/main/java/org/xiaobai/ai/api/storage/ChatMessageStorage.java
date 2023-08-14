package org.xiaobai.ai.api.storage;

import lombok.Builder;
import lombok.Data;
import org.xiaobai.ai.api.parser.ResponseParser;
import org.xiaobai.ai.entity.ChatMessageVO;

/**
 * 聊天消息数据存储业业务参数
 */
@Data
@Builder
public class ChatMessageStorage {

    /**
     * 问题聊天记录
     */
    private ChatMessageVO questionChatMessageVO;

    /**
     * 回答聊天记录
     */
    private ChatMessageVO answerChatMessageVO;

    /**
     * 原始请求数据
     */
    private String originalRequestData;

    /**
     * 原始响应数据
     */
    private String originalResponseData;

    /**
     * 异常响应数据
     */
    private String errorResponseData;

    /**
     * 响应解析器
     */
    private ResponseParser<?> parser;

    /**
     * 当前消息流条数
     */
    private int currentStreamMessageCount;
}
