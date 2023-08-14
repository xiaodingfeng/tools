package org.xiaobai.ai.handler.emitter;

import cn.hutool.core.util.StrUtil;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.accesstoken.AccessTokenApiClient;
import org.xiaobai.ai.api.accesstoken.ConversationRequest;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.ai.api.enums.ConversationModelEnum;
import org.xiaobai.ai.api.listener.ParsedEventSourceListener;
import org.xiaobai.ai.api.listener.ResponseBodyEmitterStreamListener;
import org.xiaobai.ai.api.listener.StreamCompleteListener;
import org.xiaobai.ai.api.listener.WebSockListener;
import org.xiaobai.ai.api.parser.AccessTokenChatResponseParser;
import org.xiaobai.ai.api.storage.AccessTokenDatabaseDataStorage;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.core.config.ChatConfig;
import org.xiaobai.core.utils.ObjectMapperUtil;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

/**
 * AccessToken 响应处理
 */
@Component
public class AccessTokenResponseEmitter implements ResponseEmitter {

    @Resource
    private ChatConfig chatConfig;

    @Resource
    private ChatMessageService chatMessageService;

    @Resource
    private AccessTokenChatResponseParser parser;

    @Resource
    private AccessTokenDatabaseDataStorage dataStorage;

    @Override
    public void requestToResponseEmitter(ChatProcessRequest chatProcessRequest, ResponseBodyEmitter emitter) {
        // 构建 accessTokenApiClient
        AccessTokenApiClient accessTokenApiClient = AccessTokenApiClient.builder()
                .accessToken(chatConfig.getOpenaiAccessToken())
                .reverseProxy(chatConfig.getApiReverseProxy())
                .model(chatConfig.getOpenaiApiModel())
                .build();

        // 初始化聊天消息
        ChatMessageVO chatMessageVO = chatMessageService.initChatMessage(chatProcessRequest, ApiTypeEnum.ACCESS_TOKEN);

        // 构建 ConversationRequest
        ConversationRequest conversationRequest = buildConversationRequest(chatMessageVO);

        // 构建事件监听器
        ParsedEventSourceListener parsedEventSourceListener = new ParsedEventSourceListener.Builder()
                //.addListener(new ConsoleStreamListener())
                .addListener(new ResponseBodyEmitterStreamListener(emitter))
                .addListener(new StreamCompleteListener())
                .addListener(new WebSockListener(chatProcessRequest))
                .setParser(parser)
                .setDataStorage(dataStorage)
                .setOriginalRequestData(ObjectMapperUtil.toJson(conversationRequest))
                .setChatMessageVO(chatMessageVO)
                .build();

        // 发送请求
        accessTokenApiClient.streamChatCompletions(conversationRequest, parsedEventSourceListener);
    }

    /**
     * 构建 ConversationRequest
     *
     * @param chatMessageVO 聊天消息
     * @return ConversationRequest
     */
    private ConversationRequest buildConversationRequest(ChatMessageVO chatMessageVO) {
        String modelPrompts = chatMessageService.handlerModelPrompts(chatMessageVO.getChatRoomId());
        if (modelPrompts.startsWith("You are ChatGPT, a large language model")) {
            modelPrompts = "";
        }
        // 构建 content
        ConversationRequest.Content content = ConversationRequest.Content.builder()
                .parts(Collections.singletonList(modelPrompts + chatMessageVO.getContent()))
                .build();

        // 构建 Message
        ConversationRequest.Message message = ConversationRequest.Message.builder()
                .id(chatMessageVO.getMessageId())
                .role(Message.Role.USER.getName())
                .content(content)
                .build();

        // 构建 ConversationRequest
        String model = chatConfig.getOpenaiApiModel();

        return ConversationRequest.builder()
                .messages(Collections.singletonList(message))
                .action(ConversationRequest.MessageActionTypeEnum.NEXT)
                .model(ConversationModelEnum.NAME_MAP.get(model))
                // 父级消息 id 不能为空，不然会报错，因此第一条消息也需要随机生成一个
                .parentMessageId(StrUtil.isBlank(chatMessageVO.getParentMessageId())
                        ? UUID.randomUUID().toString() : chatMessageVO.getParentMessageId())
                .conversationId(chatMessageVO.getConversationId())
                .build();
    }
}
