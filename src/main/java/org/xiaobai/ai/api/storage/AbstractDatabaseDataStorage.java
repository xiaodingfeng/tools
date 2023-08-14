package org.xiaobai.ai.api.storage;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.xiaobai.ai.api.enums.ChatMessageStatusEnum;
import org.xiaobai.ai.api.enums.ChatMessageTypeEnum;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.entity.ChatRoomVO;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.ai.service.ChatRoomService;
import org.xiaobai.ai.service.ChatUserBalanceService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 数据库数据存储抽象类
 */
@Slf4j
public abstract class AbstractDatabaseDataStorage implements DataStorage {

    @Resource
    protected ChatMessageService chatMessageService;

    @Resource
    protected ChatRoomService chatRoomService;

    @Resource
    protected ChatUserBalanceService chatUserBalanceService;

    @Override
    public void onMessage(ChatMessageStorage chatMessageStorage) {
        // 处理第一条消息
        if (chatMessageStorage.getCurrentStreamMessageCount() != 1) {
            return;
        }
        ChatMessageVO questionChatMessageVO = chatMessageStorage.getQuestionChatMessageVO();
        ChatMessageVO answerChatMessageVO = chatMessageStorage.getAnswerChatMessageVO();
        answerChatMessageVO.setParentMessageId(questionChatMessageVO.getMessageId());
        answerChatMessageVO.setUserId(questionChatMessageVO.getUserId());
        answerChatMessageVO.setParentAnswerMessageId(questionChatMessageVO.getParentAnswerMessageId());
        answerChatMessageVO.setParentQuestionMessageId(questionChatMessageVO.getMessageId());
        answerChatMessageVO.setContextCount(questionChatMessageVO.getContextCount());
        answerChatMessageVO.setQuestionContextCount(questionChatMessageVO.getQuestionContextCount());
        answerChatMessageVO.setModelName(questionChatMessageVO.getModelName());
        answerChatMessageVO.setMessageType(ChatMessageTypeEnum.ANSWER);
        answerChatMessageVO.setChatRoomId(questionChatMessageVO.getChatRoomId());
        answerChatMessageVO.setApiType(questionChatMessageVO.getApiType());
        answerChatMessageVO.setApiKey(questionChatMessageVO.getApiKey());
        answerChatMessageVO.setOriginalData(chatMessageStorage.getOriginalResponseData());
        answerChatMessageVO.setStatus(ChatMessageStatusEnum.PART_SUCCESS);
        answerChatMessageVO.setIp(questionChatMessageVO.getIp());
        answerChatMessageVO.setCreateTime(new Date());
        answerChatMessageVO.setUpdateTime(new Date());

        // 填充第一条消息的字段
        onFirstMessage(chatMessageStorage);

        // 保存回答消息记录
        chatMessageService.save(answerChatMessageVO);

        // 聊天室更新 conversationId
        chatRoomService.update(new LambdaUpdateWrapper<ChatRoomVO>()
                .set(ChatRoomVO::getConversationId, answerChatMessageVO.getConversationId())
                .eq(ChatRoomVO::getId, answerChatMessageVO.getChatRoomId()));
        if (!chatUserBalanceService.autoReduceChatBalance(questionChatMessageVO.getUserId())) {
            log.info("额度减少失败：userId: {}, messageId: {}", questionChatMessageVO.getUserId(), answerChatMessageVO.getMessageId());
        }
    }

    /**
     * 收到第一条消息
     *
     * @param chatMessageStorage 聊天记录存储
     */
    abstract void onFirstMessage(ChatMessageStorage chatMessageStorage);

    /**
     * 收到最后第一条消息
     *
     * @param chatMessageStorage 聊天记录存储
     */
    abstract void onLastMessage(ChatMessageStorage chatMessageStorage);

    /**
     * 收到错误消息
     *
     * @param chatMessageStorage 聊天记录存储
     */
    abstract void onErrorMessage(ChatMessageStorage chatMessageStorage);

    @Override
    public void onComplete(ChatMessageStorage chatMessageStorage) {
        ChatMessageVO questionChatMessageVO = chatMessageStorage.getQuestionChatMessageVO();
        ChatMessageVO answerChatMessageVO = chatMessageStorage.getAnswerChatMessageVO();

        // 成功状态
        questionChatMessageVO.setStatus(ChatMessageStatusEnum.COMPLETE_SUCCESS);
        answerChatMessageVO.setStatus(ChatMessageStatusEnum.COMPLETE_SUCCESS);

        // 原始请求数据
        questionChatMessageVO.setOriginalData(chatMessageStorage.getOriginalRequestData());

        // 原始响应数据
        answerChatMessageVO.setOriginalData(chatMessageStorage.getOriginalResponseData());

        // 更新时间
        questionChatMessageVO.setUpdateTime(new Date());
        answerChatMessageVO.setUpdateTime(new Date());

        // 最后一条消息
        onLastMessage(chatMessageStorage);

        // 更新消息
        chatMessageService.updateById(questionChatMessageVO);
        chatMessageService.updateById(answerChatMessageVO);
    }

    @Override
    public void onError(ChatMessageStorage chatMessageStorage) {
        // 消息流条数大于 0 表示部分成功
        ChatMessageStatusEnum chatMessageStatusEnum = chatMessageStorage.getCurrentStreamMessageCount() > 0 ? ChatMessageStatusEnum.PART_SUCCESS : ChatMessageStatusEnum.ERROR;

        // 填充问题消息记录
        ChatMessageVO questionChatMessageVO = chatMessageStorage.getQuestionChatMessageVO();
        questionChatMessageVO.setStatus(chatMessageStatusEnum);
        // 原始请求数据
        questionChatMessageVO.setOriginalData(chatMessageStorage.getOriginalRequestData());
        // 错误响应数据
        questionChatMessageVO.setResponseErrorData(chatMessageStorage.getErrorResponseData());
        questionChatMessageVO.setUpdateTime(new Date());

        // 还没收到回复就断了，跳过回答消息记录更新
        if (chatMessageStatusEnum != ChatMessageStatusEnum.ERROR) {
            // 填充问题消息记录
            ChatMessageVO answerChatMessageVO = chatMessageStorage.getAnswerChatMessageVO();
            answerChatMessageVO.setStatus(chatMessageStatusEnum);
            // 原始响应数据
            answerChatMessageVO.setOriginalData(chatMessageStorage.getOriginalResponseData());
            // 错误响应数据
            answerChatMessageVO.setResponseErrorData(chatMessageStorage.getErrorResponseData());
            // 更新时间
            answerChatMessageVO.setUpdateTime(new Date());
        }

        // 填充错误消息
        onErrorMessage(chatMessageStorage);

        // 更新错误的问题消息记录
        chatMessageService.updateById(chatMessageStorage.getQuestionChatMessageVO());
        // 更新错误的回答消息记录
        if (chatMessageStatusEnum != ChatMessageStatusEnum.ERROR) {
            chatMessageService.updateById(chatMessageStorage.getAnswerChatMessageVO());
        }
    }
}
