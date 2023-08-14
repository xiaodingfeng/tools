package org.xiaobai.ai.api.storage;

import cn.hutool.core.util.StrUtil;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import org.springframework.stereotype.Component;
import org.xiaobai.ai.api.enums.ApiKeyModelEnum;
import org.xiaobai.ai.entity.ChatMessageVO;

import java.util.UUID;

/**
 * ApiKey 数据库数据存储
 */
@Component
public class ApiKeyDatabaseDataStorage extends AbstractDatabaseDataStorage {

    @Override
    public void onFirstMessage(ChatMessageStorage chatMessageStorage) {
        // 第一条消息手动生成消息 id 和对话 id
        ChatMessageVO answerChatMessageVO = chatMessageStorage.getAnswerChatMessageVO();
        answerChatMessageVO.setMessageId(UUID.randomUUID().toString());
        answerChatMessageVO.setConversationId(UUID.randomUUID().toString());
    }

    @Override
    void onLastMessage(ChatMessageStorage chatMessageStorage) {
        populateMessageUsageToken(chatMessageStorage);
    }

    @Override
    void onErrorMessage(ChatMessageStorage chatMessageStorage) {
        populateMessageUsageToken(chatMessageStorage);
    }

    /**
     * 填充消息使用 Token 数量
     *
     * @param chatMessageStorage 聊天消息数据存储
     */
    private void populateMessageUsageToken(ChatMessageStorage chatMessageStorage) {
        // 获取模型
        ChatMessageVO questionChatMessageVO = chatMessageStorage.getQuestionChatMessageVO();
        String modelName = questionChatMessageVO.getModelName();

        // 获取回答消耗的 tokens
        ChatMessageVO answerChatMessageVO = chatMessageStorage.getAnswerChatMessageVO();
        String answerContent = answerChatMessageVO.getContent();
        int completionTokens = StrUtil.isEmpty(answerContent) ? 0 : TikTokensUtil.tokens(ApiKeyModelEnum.NAME_MAP.get(modelName).getCalcTokenModelName(), answerContent);

        // 填充使用情况
        int totalTokens = questionChatMessageVO.getPromptTokens() + completionTokens;
        answerChatMessageVO.setPromptTokens(questionChatMessageVO.getPromptTokens());
        answerChatMessageVO.setCompletionTokens(completionTokens);
        answerChatMessageVO.setTotalTokens(totalTokens);

        answerChatMessageVO.setCompletionTokens(completionTokens);
        answerChatMessageVO.setTotalTokens(totalTokens);
    }
}
