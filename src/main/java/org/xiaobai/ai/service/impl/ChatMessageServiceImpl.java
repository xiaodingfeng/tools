package org.xiaobai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.ai.api.enums.ChatMessageStatusEnum;
import org.xiaobai.ai.api.enums.ChatMessageTypeEnum;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.entity.ChatRoomVO;
import org.xiaobai.ai.handler.emitter.*;
import org.xiaobai.ai.mapper.ChatMessageMapper;
import org.xiaobai.ai.request.query.ChatMessagePageQuery;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.ai.service.ChatModelService;
import org.xiaobai.ai.service.ChatRoomService;
import org.xiaobai.common.entity.BaseEntity;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.config.ChatConfig;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.ObjectMapperUtil;
import org.xiaobai.core.utils.PageUtil;
import org.xiaobai.core.utils.WebUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 聊天记录相关业务实现类
 */
@Slf4j
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessageVO> implements ChatMessageService {

    @Resource
    private ChatConfig chatConfig;

    @Resource
    private ChatRoomService chatRoomService;

    @Resource
    private ChatModelService chatModelService;

    @Override
    public ResponseBodyEmitter sendMessage(ChatProcessRequest chatProcessRequest) {
        // 超时时间设置 3 分钟
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        emitter.onCompletion(() -> log.debug("请求参数：{}，Front-end closed the emitter connection.", ObjectMapperUtil.toJson(chatProcessRequest)));
        emitter.onTimeout(() -> log.error("请求参数：{}，Back-end closed the emitter connection.", ObjectMapperUtil.toJson(chatProcessRequest)));
        // 构建 emitter 处理链路
        ResponseEmitterChain ipRateLimiterEmitterChain = new IpRateLimiterEmitterChain();
        ResponseEmitterChain sensitiveWordEmitterChain = new SensitiveWordEmitterChain();
        ResponseEmitterChain chatLimiterEmitterChain = new ChatLimiterEmitterChain();
        sensitiveWordEmitterChain.setNext(new ChatMessageEmitterChain());
        ipRateLimiterEmitterChain.setNext(sensitiveWordEmitterChain);
        chatLimiterEmitterChain.setNext(ipRateLimiterEmitterChain);
        chatLimiterEmitterChain.doChain(chatProcessRequest, emitter);
        return emitter;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChatMessageVO initChatMessage(ChatProcessRequest chatProcessRequest, ApiTypeEnum apiTypeEnum) {
        ChatMessageVO chatMessageVO = new ChatMessageVO();
        chatMessageVO.setId(IdWorker.getId());
        // 消息 id 手动生成
        chatMessageVO.setMessageId(UUID.randomUUID().toString());
        chatMessageVO.setMessageType(ChatMessageTypeEnum.QUESTION);
        chatMessageVO.setApiType(apiTypeEnum);
        if (apiTypeEnum == ApiTypeEnum.API_KEY) {
            chatMessageVO.setApiKey(chatConfig.getOpenaiApiKey());
        }
        chatMessageVO.setUserId(chatProcessRequest.getUserId());
        chatMessageVO.setContent(chatProcessRequest.getPrompt());
        chatMessageVO.setModelName(chatConfig.getOpenaiApiModel());
        chatMessageVO.setOriginalData(null);
        chatMessageVO.setPromptTokens(-1);
        chatMessageVO.setCompletionTokens(-1);
        chatMessageVO.setTotalTokens(-1);
        chatMessageVO.setIp(WebUtil.getIp());
        chatMessageVO.setStatus(ChatMessageStatusEnum.INIT);
        chatMessageVO.setCreateTime(new Date());
        chatMessageVO.setUpdateTime(new Date());

        // 填充初始化父级消息参数
        populateInitParentMessage(chatMessageVO, chatProcessRequest);

        save(chatMessageVO);
        return chatMessageVO;
    }

    /**
     * 填充初始化父级消息参数
     *
     * @param chatMessageVO      消息记录
     * @param chatProcessRequest 消息处理请求参数
     */
    private void populateInitParentMessage(ChatMessageVO chatMessageVO, ChatProcessRequest chatProcessRequest) {
        // 房间 id
        Long roomId = Optional.ofNullable(chatProcessRequest.getOptions())
                .map(ChatProcessRequest.Options::getRoomId)
                .orElse(null);
        ChatRoomVO chatRoom;
        if (Objects.nonNull(roomId)) {
            chatRoom = chatRoomService.getById(roomId);
            if (Objects.isNull(chatRoom)) {
                throw new TipException("会话不存在，本次对话出错，请先关闭上下文或开启新的对话窗口");
            }
        } else {
            throw new TipException("会话不存在，本次对话出错，请先关闭上下文或开启新的对话窗口");
        }
        // 父级消息 id
        String parentMessageId = Optional.ofNullable(chatProcessRequest.getOptions())
                .map(ChatProcessRequest.Options::getParentMessageId)
                .orElse(null);

        // 对话 id
        String conversationId = Optional.ofNullable(chatProcessRequest.getOptions())
                .map(ChatProcessRequest.Options::getConversationId)
                .orElse(null);

        if (StrUtil.isAllNotBlank(parentMessageId, conversationId)) {
            // 寻找父级消息
            ChatMessageVO parentChatMessage = getOne(new LambdaQueryWrapper<ChatMessageVO>()
                    // 用户 id 一致
                    .eq(ChatMessageVO::getUserId, UserUtil.getUserId())
                    // 消息 id 一致
                    .eq(ChatMessageVO::getMessageId, parentMessageId)
                    // 对话 id 一致
                    .eq(ChatMessageVO::getConversationId, conversationId)
                    // Api 类型一致
                    .eq(ChatMessageVO::getApiType, chatMessageVO.getApiType())
                    // 消息类型为回答
                    .eq(ChatMessageVO::getMessageType, ChatMessageTypeEnum.ANSWER));
            if (Objects.isNull(parentChatMessage)) {
                throw new TipException("父级消息不存在，本次对话出错，请先关闭上下文或开启新的对话窗口");
            }

            chatMessageVO.setParentMessageId(parentMessageId);
            chatMessageVO.setParentAnswerMessageId(parentMessageId);
            chatMessageVO.setParentQuestionMessageId(parentChatMessage.getParentQuestionMessageId());
            chatMessageVO.setChatRoomId(parentChatMessage.getChatRoomId());
            chatMessageVO.setConversationId(parentChatMessage.getConversationId());
            chatMessageVO.setContextCount(parentChatMessage.getContextCount() + 1);
            chatMessageVO.setQuestionContextCount(parentChatMessage.getQuestionContextCount() + 1);

            if (chatMessageVO.getApiType() == ApiTypeEnum.ACCESS_TOKEN) {
                if (!Objects.equals(chatMessageVO.getModelName(), parentChatMessage.getModelName())) {
                    throw new TipException(StrUtil.format("当前对话类型为 AccessToken 使用模型不一样，请开启新的对话"));
                }
            }

            // ApiKey 限制上下文问题的数量
            if (chatMessageVO.getApiType() == ApiTypeEnum.API_KEY
                    && chatConfig.getLimitQuestionContextCount() > 0
                    && chatMessageVO.getQuestionContextCount() > chatConfig.getLimitQuestionContextCount()) {
                throw new TipException(StrUtil.format("当前允许连续对话的问题数量为[{}]次，已达到上限，请关闭上下文对话重新发送", chatConfig.getLimitQuestionContextCount()));
            }
        } else {
            chatRoom.setFirstChatMessageId(chatMessageVO.getId());
            chatRoom.setFirstMessageId(chatMessageVO.getMessageId());
            chatRoomService.updateById(chatRoom);
            chatMessageVO.setContextCount(1);
            chatMessageVO.setQuestionContextCount(1);
            chatMessageVO.setChatRoomId(chatRoom.getId());
        }
    }

    @Override
    public IPage<ChatMessageVO> pageChatMessage(ChatMessagePageQuery chatMessagePageQuery) {
        Page<ChatMessageVO> chatMessagePage = page(new Page<>(chatMessagePageQuery.getPageNum()
                , chatMessagePageQuery.getPageSize()), new LambdaQueryWrapper<ChatMessageVO>()
                // 聊天内容模糊查询
                .like(StrUtil.isNotBlank(chatMessagePageQuery.getContent()), ChatMessageVO::getContent
                        , chatMessagePageQuery.getContent())
                // IP 模糊查询
                .like(StrUtil.isNotBlank(chatMessagePageQuery.getIp())
                        , ChatMessageVO::getIp, chatMessagePageQuery.getIp())
                // 查询指定聊天室
                .eq(Objects.nonNull(chatMessagePageQuery.getChatRoomId())
                        , ChatMessageVO::getChatRoomId, chatMessagePageQuery.getChatRoomId())
                .orderByDesc(ChatMessageVO::getCreateTime));

        return PageUtil.toPage(chatMessagePage, chatMessagePage.getRecords());
    }

    @Override
    public List<Long> findRoomIdByKeyWords(String keywords) {
        return list(new LambdaQueryWrapper<ChatMessageVO>()
                .select(ChatMessageVO::getChatRoomId)
                .like(ChatMessageVO::getContent, keywords))
                .stream().map(ChatMessageVO::getChatRoomId).collect(Collectors.toList());
    }

    public String handlerModelPrompts(Long roomId) {
        String defaultPrompts = "You are ChatGPT, a large language model trained by OpenAI. Answer as concisely as possible.\\\\nKnowledge cutoff: 2021-09-01\\\\nCurrent date: \".concat(DateUtil.today())";
        ChatRoomVO roomVO = chatRoomService.getById(roomId);
        if (Objects.isNull(roomVO)) {
            return defaultPrompts;
        }
        ChatModelVO modelVO = chatModelService.getById(roomVO.getModelId());
        if (Objects.isNull(modelVO) || StrUtil.isEmpty(modelVO.getSystemPrompts())) {
            return defaultPrompts;
        }
        return modelVO.getSystemPrompts();
    }

    @Override
    public long getUserUsedRangeTime(Date start, Date end) {
        return count(new LambdaQueryWrapper<ChatMessageVO>()
        .eq(ChatMessageVO::getUserId, UserUtil.getUserId())
        .lt(BaseEntity::getCreateTime, end)
        .ge(BaseEntity::getCreateTime, start));
    }

    @Override
    public ResponseBodyEmitter sendWebSocketMessage(ChatProcessRequest chatProcessRequest) {
        // 超时时间设置 3 分钟
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        emitter.onCompletion(() -> log.debug("请求参数：{}，Front-end closed the emitter connection.", ObjectMapperUtil.toJson(chatProcessRequest)));
        emitter.onTimeout(() -> log.error("请求参数：{}，Back-end closed the emitter connection.", ObjectMapperUtil.toJson(chatProcessRequest)));
        // 构建 emitter 处理链路
        ResponseEmitterChain sensitiveWordEmitterChain = new SensitiveWordEmitterChain();
        ResponseEmitterChain chatLimiterEmitterChain = new ChatLimiterEmitterChain();
        sensitiveWordEmitterChain.setNext(new ChatMessageEmitterChain());
        chatLimiterEmitterChain.setNext(sensitiveWordEmitterChain);
        chatLimiterEmitterChain.doChain(chatProcessRequest, emitter);
        return emitter;
    }
}
