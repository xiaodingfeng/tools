package org.xiaobai.ai.api.domain.vo;

import lombok.Data;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;

import java.util.Optional;

/**
 * 聊天回复的消息
 */
@Data
public class ChatReplyMessageVO {

    /**
     * 对于前端有什么用？
     */
    private String role;

    private String id;

    private String parentMessageId;

    private String conversationId;

    private String text;

    /**
     * 当链路出现问题时 取上一条消息的 parentMessageId 和 conversationId，使得异常不影响上下文
     *
     * @param request 消息处理请求的实体 从中获取 parentMessageId 和 conversationId
     * @return 聊天回复的消息
     */
    public static ChatReplyMessageVO onEmitterChainException(ChatProcessRequest request) {
        ChatProcessRequest.Options options = request.getOptions();
        ChatReplyMessageVO chatReplyMessageVO = new ChatReplyMessageVO();
        chatReplyMessageVO.setId(Optional.of(options).orElse(new ChatProcessRequest.Options()).getParentMessageId());
        chatReplyMessageVO.setConversationId(Optional.of(options).orElse(new ChatProcessRequest.Options()).getConversationId());
        chatReplyMessageVO.setParentMessageId(null);
        return chatReplyMessageVO;
    }
}
