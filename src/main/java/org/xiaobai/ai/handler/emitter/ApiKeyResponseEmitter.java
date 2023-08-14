package org.xiaobai.ai.handler.emitter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.apikey.ApiKeyChatClientBuilder;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.api.domain.vo.ChatReplyMessageVO;
import org.xiaobai.ai.api.enums.ApiKeyModelEnum;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.ai.api.enums.ChatMessageStatusEnum;
import org.xiaobai.ai.api.enums.ChatMessageTypeEnum;
import org.xiaobai.ai.api.listener.ParsedEventSourceListener;
import org.xiaobai.ai.api.listener.ResponseBodyEmitterStreamListener;
import org.xiaobai.ai.api.listener.WebSockListener;
import org.xiaobai.ai.api.parser.ChatCompletionResponseParser;
import org.xiaobai.ai.api.storage.ApiKeyDatabaseDataStorage;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.core.config.ChatConfig;
import org.xiaobai.core.utils.ObjectMapperUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

/**
 * ApiKey 响应处理
 */
@Component
public class ApiKeyResponseEmitter implements ResponseEmitter {

    @Resource
    private ChatConfig chatConfig;

    @Resource
    private ChatMessageService chatMessageService;

    @Resource
    private ChatCompletionResponseParser parser;

    @Resource
    private ApiKeyDatabaseDataStorage dataStorage;

    @Override
    public void requestToResponseEmitter(ChatProcessRequest chatProcessRequest, ResponseBodyEmitter emitter) {
        // 初始化聊天消息
        ChatMessageVO chatMessageVO = chatMessageService.initChatMessage(chatProcessRequest, ApiTypeEnum.API_KEY);

        // 所有消息
        LinkedList<Message> messages = new LinkedList<>();
        // 添加用户上下文消息
        addContextChatMessage(chatMessageVO, messages);

        String modelPrompts = chatMessageService.handlerModelPrompts(chatMessageVO.getChatRoomId());
        // 系统角色消息
        if (StrUtil.isNotBlank(modelPrompts)) {
            // 系统消息
            Message systemMessage = Message.builder()
                    .role(Message.Role.SYSTEM)
                    .content(modelPrompts)
                    .build();
            messages.addFirst(systemMessage);
        }

        // 获取 包含上下文 的 token 数量
        int totalTokenCount = TikTokensUtil.tokens(ApiKeyModelEnum.NAME_MAP.get(chatMessageVO.getModelName()).getCalcTokenModelName(), messages);
        // 设置 promptTokens
        chatMessageVO.setPromptTokens(totalTokenCount);

        // 检查 tokenCount 是否超出当前模型的 Token 数量限制
        String exceedModelTokenLimitMsg = exceedModelTokenLimit(chatProcessRequest, chatMessageVO.getModelName(), totalTokenCount, emitter);
        if (Objects.nonNull(exceedModelTokenLimitMsg)) {
            chatMessageVO.setStatus(ChatMessageStatusEnum.EXCEPTION_TOKEN_EXCEED_LIMIT);
            chatMessageVO.setResponseErrorData(exceedModelTokenLimitMsg);
            chatMessageService.updateById(chatMessageVO);
            return;
        }

        // 构建聊天参数
        ChatCompletion chatCompletion = ChatCompletion.builder()
                // 最大的 tokens = 模型的最大上线 - 本次 prompt 消耗的 tokens
                .maxTokens(ApiKeyModelEnum.maxTokens(chatConfig.getOpenaiApiModel()) - totalTokenCount - 1)
                .model(chatConfig.getOpenaiApiModel())
                // [0, 2] 越低越精准
                .temperature(0.8)
                .topP(1.0)
                // 每次生成一条
                .n(1)
                .presencePenalty(1)
                .messages(messages)
                .stream(true)
                .build();

        // 构建事件监听器
        ParsedEventSourceListener parsedEventSourceListener = new ParsedEventSourceListener.Builder()
//                .addListener(new ConsoleStreamListener())
                .addListener(new ResponseBodyEmitterStreamListener(emitter))
                .addListener(new WebSockListener(chatProcessRequest))
                .setParser(parser)
                .setDataStorage(dataStorage)
                .setOriginalRequestData(ObjectMapperUtil.toJson(chatCompletion))
                .setChatMessageVO(chatMessageVO)
                .build();

        ApiKeyChatClientBuilder.buildOpenAiStreamClient().streamChatCompletion(chatCompletion, parsedEventSourceListener);
    }

    /**
     * 检查上下文消息的 Token 数是否超出模型限制
     *
     * @param chatProcessRequest 对话请求
     * @param modelName          当前使用的模型名称
     * @param tokenCount         当前上下的总 Token 数量
     * @param emitter            ResponseBodyEmitter
     */
    private String exceedModelTokenLimit(ChatProcessRequest chatProcessRequest, String modelName, int tokenCount, ResponseBodyEmitter emitter) {
        // 当前模型最大 tokens
        int maxTokens = ApiKeyModelEnum.maxTokens(modelName);

        String msg;
        // 判断 token 数量是否超过限制
        if (tokenCount >= maxTokens) {
            // 获取当前 prompt 消耗的 tokens
            int currentPromptTokens = TikTokensUtil.tokens(ApiKeyModelEnum.NAME_MAP.get(modelName).getCalcTokenModelName(), chatProcessRequest.getPrompt());
            // 判断历史上下文是否超过限制
            int remainingTokens = tokenCount - currentPromptTokens;
            if (remainingTokens >= maxTokens) {
                msg = "当前上下文字数已经达到上限，请关闭上下文或开启新的对话";
            } else {
                msg = StrUtil.format("当前上下文 Token 数量：{}，超过上限：{}，请减少字数发送或关闭上下文或开启新的对话", tokenCount, maxTokens);
            }
        }
        // 剩余的 token 太少也直返返回异常信息
        else if (maxTokens - tokenCount <= 10) {
            msg = "当前上下文字数不足以连续对话，请关闭上下文或开启新的对话";
        } else {
            return null;
        }

        try {
            ChatReplyMessageVO chatReplyMessageVO = ChatReplyMessageVO.onEmitterChainException(chatProcessRequest);
            chatReplyMessageVO.setText(msg);
            emitter.send(ObjectMapperUtil.toJson(chatReplyMessageVO));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            emitter.complete();
        }
        return msg;
    }

    /**
     * 添加上下文问题消息
     *
     * @param chatMessageVO 当前消息
     * @param messages      消息列表
     */
    private void addContextChatMessage(ChatMessageVO chatMessageVO, LinkedList<Message> messages) {
        if (Objects.isNull(chatMessageVO)) {
            return;
        }
        // 父级消息id为空，表示是第一条消息，直接添加到message里
        if (Objects.isNull(chatMessageVO.getParentMessageId())) {
            messages.addFirst(Message.builder().role(Message.Role.USER)
                    .content(chatMessageVO.getContent())
                    .build());
            return;
        }

        // 根据消息类型去选择角色，需要添加问题和回答到上下文
        Message.Role role = (chatMessageVO.getMessageType() == ChatMessageTypeEnum.ANSWER) ?
                Message.Role.ASSISTANT : Message.Role.USER;

        // 回答不成功的情况下，不添加回答消息记录和该回答的问题消息记录
        if (chatMessageVO.getMessageType() == ChatMessageTypeEnum.ANSWER
                && chatMessageVO.getStatus() != ChatMessageStatusEnum.PART_SUCCESS
                && chatMessageVO.getStatus() != ChatMessageStatusEnum.COMPLETE_SUCCESS) {
            // 没有父级回答消息直接跳过
            if (Objects.isNull(chatMessageVO.getParentAnswerMessageId())) {
                return;
            }
            ChatMessageVO parentMessage = chatMessageService.getOne(new LambdaQueryWrapper<ChatMessageVO>()
                    .eq(ChatMessageVO::getMessageId, chatMessageVO.getParentAnswerMessageId()));
            addContextChatMessage(parentMessage, messages);
            return;
        }

        // 从下往上找并添加，越上面的数据放越前面
        messages.addFirst(Message.builder().role(role)
                .content(chatMessageVO.getContent())
                .build());
        ChatMessageVO parentMessage = chatMessageService.getOne(new LambdaQueryWrapper<ChatMessageVO>()
                .eq(ChatMessageVO::getMessageId, chatMessageVO.getParentMessageId()));
        addContextChatMessage(parentMessage, messages);
    }
}
