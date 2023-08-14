package org.xiaobai.ai.api.parser;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import org.xiaobai.ai.api.accesstoken.ConversationResponse;
import org.xiaobai.ai.api.dto.Message;
import org.xiaobai.core.utils.ObjectMapperUtil;

import java.util.List;
import java.util.Objects;

/**
 * AccessToken 的聊天对话解析器
 */
@Component
public class AccessTokenChatResponseParser implements ResponseParser<ConversationResponse> {

    @Override
    public ConversationResponse parseSuccess(String originalData) {
        return ObjectMapperUtil.fromJson(originalData, ConversationResponse.class);
    }

    @Override
    public String parseReceivedMessage(String receivedMessage, String newMessage) {
        return newMessage;
    }

    @Override
    public String parseNewMessage(String originalData) {
        // 不为 JSON 直接返回 null，不知道什么情况触发，但是不属于正文
        if (!JSONUtil.isTypeJSON(originalData)) {
            return null;
        }
        ConversationResponse.Message message = parseSuccess(originalData).getMessage();
        if (Objects.isNull(message)) {
            return null;
        }
        ConversationResponse.Author author = message.getAuthor();
        if (!author.getRole().equals(Message.Role.ASSISTANT.getName())) {
            return null;
        }

        // 只需要 role=assistant 的消息
        List<String> parts = message.getContent().getParts();
        if (CollectionUtil.isEmpty(parts)) {
            return null;
        }

        // AccessToken 模式返回的消息每句都会包含前面的话，不需要手动拼接
        return parts.get(0);
    }
}