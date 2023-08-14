package org.xiaobai.ai.api.parser;

import cn.hutool.core.collection.CollectionUtil;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.stereotype.Component;
import org.xiaobai.core.utils.ObjectMapperUtil;

import java.util.List;
import java.util.Objects;

/**
 * ApiKey 的 ChatCompletion 响应解析器
 */
@Component
public class ChatCompletionResponseParser implements ResponseParser<ChatCompletionResponse> {

    @Override
    public ChatCompletionResponse parseSuccess(String originalData) {
        return ObjectMapperUtil.fromJson(originalData, ChatCompletionResponse.class);
    }

    @Override
    public String parseReceivedMessage(String receivedMessage, String newMessage) {
        return receivedMessage.concat(newMessage);
    }

    @Override
    public String parseNewMessage(String originalData) {
        Message message = getMessage(originalData);
        if (Objects.isNull(message)) {
            return null;
        }
        return message.getContent();
    }

    /**
     * 获取消息
     *
     * @param originalData 原始数据
     * @return 消息
     */
    private Message getMessage(String originalData) {
        ChatCompletionResponse response = parseSuccess(originalData);
        List<ChatChoice> choices = response.getChoices();
        if (CollectionUtil.isEmpty(choices)) {
            return null;
        }
        return choices.get(0).getDelta();
    }
}
