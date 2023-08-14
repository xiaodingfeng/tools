package org.xiaobai.ai.api.storage;

import org.springframework.stereotype.Component;
import org.xiaobai.ai.api.accesstoken.ConversationResponse;
import org.xiaobai.ai.entity.ChatMessageVO;

/**
 * AccessToken 数据库数据存储
 */
@Component
public class AccessTokenDatabaseDataStorage extends AbstractDatabaseDataStorage {

    @Override
    public void onFirstMessage(ChatMessageStorage chatMessageStorage) {
        // 第一条消息
        ConversationResponse conversationResponse = (ConversationResponse) chatMessageStorage.getParser().
                parseSuccess(chatMessageStorage.getOriginalResponseData());
        ConversationResponse.Message message = conversationResponse.getMessage();

        // 第一条消息填充对话 id 和消息 id
        ChatMessageVO answerChatMessageVO = chatMessageStorage.getAnswerChatMessageVO();
        answerChatMessageVO.setMessageId(message.getId());
        answerChatMessageVO.setConversationId(conversationResponse.getConversationId());

        // 填充问题消息的对话 id
        chatMessageStorage.getQuestionChatMessageVO().setConversationId(conversationResponse.getConversationId());
    }

    @Override
    void onLastMessage(ChatMessageStorage chatMessageStorage) {

    }

    @Override
    void onErrorMessage(ChatMessageStorage chatMessageStorage) {

    }
}
