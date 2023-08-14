package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.ai.api.enums.ChatMessageStatusEnum;
import org.xiaobai.ai.api.enums.ChatMessageTypeEnum;
import org.xiaobai.common.entity.BaseEntity;

/**
 * 聊天记录表实体类
 */
@Data
@TableName("t_chat_message")
public class ChatMessageVO extends BaseEntity {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 消息 id
     */
    private String messageId;

    /**
     * 父级消息 id
     * 第一条消息父级消息 id 为空
     * 回答的父级消息 id 不能为空
     */
    private String parentMessageId;

    /**
     * 父级回答消息 id
     * 当前消息是问题：则 parentMessageId=parentAnswerMessageId
     */
    private String parentAnswerMessageId;

    /**
     * 父级问题消息 id
     * 当前消息是回答：则 parentMessageId=parentQuestionMessageId
     */
    private String parentQuestionMessageId;

    /**
     * 上下文数量
     * 包含了问题和回答的数量
     * 第一条消息是 1
     */
    private Integer contextCount;

    /**
     * 问题上下文数量
     * 包含了连续的问题的数量
     * 第一条消息是 1
     */
    private Integer questionContextCount;

    /**
     * 消息类型枚举
     * 第一条消息一定是问题
     */
    private ChatMessageTypeEnum messageType;

    /**
     * 聊天室 id
     */
    private Long chatRoomId;

    /**
     * 对话 id
     */
    private String conversationId;

    /**
     * API 类型
     */
    private ApiTypeEnum apiType;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * ip
     */
    private String ip;

    /**
     * apiKey
     */
    private String apiKey;

    /**
     * 消息内容
     * 包含上下文的对话这里只会显示出用户发送的
     */
    private String content;

    /**
     * 消息的原始数据
     * 问题：请求参数
     * 回答：响应参数
     */
    private String originalData;

    /**
     * 错误响应数据
     */
    private String responseErrorData;

    /**
     * 输入消息的 tokens
     */
    private Integer promptTokens;

    /**
     * 输出消息的 tokens
     */
    private Integer completionTokens;

    /**
     * 累计 Tokens
     */
    private Integer totalTokens;

    /**
     * 聊天信息状态
     */
    private ChatMessageStatusEnum status;

    /**
     * 是否被隐藏
     */
    private Boolean isHide;
}
