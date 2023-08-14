package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.request.query.ChatMessagePageQuery;

import java.util.Date;
import java.util.List;

/**
 * 聊天记录相关业务接口
 */
public interface ChatMessageService extends IService<ChatMessageVO> {

    /**
     * 消息处理
     *
     * @param chatProcessRequest 消息处理请求参数
     * @return emitter
     */
    ResponseBodyEmitter sendMessage(ChatProcessRequest chatProcessRequest);

    /**
     * 初始化聊天消息
     *
     * @param chatProcessRequest 消息处理请求参数
     * @param apiTypeEnum        API 类型
     * @return 聊天消息
     */
    ChatMessageVO initChatMessage(ChatProcessRequest chatProcessRequest, ApiTypeEnum apiTypeEnum);

    /**
     * 聊天记录分页
     *
     * @param chatMessagePageQuery 查询参数
     * @return 聊天记录展示参数
     */
    IPage<ChatMessageVO> pageChatMessage(ChatMessagePageQuery chatMessagePageQuery);

    /**
     * 关键词获取房间ID
     *
     * @param keywords
     * @return
     */
    List<Long> findRoomIdByKeyWords(String keywords);

    /**
     * 获取会话prompts
     *
     * @param roomId
     * @return
     */
    String handlerModelPrompts(Long roomId);

    /**
     * 获取用户时间范围内 所用记录数
     * @return 记录数
     */
    long getUserUsedRangeTime(Date start, Date end);

    /**
     * web socket 消息处理
     *
     * @param chatProcessRequest 消息处理请求参数
     * @return emitter
     */
    ResponseBodyEmitter sendWebSocketMessage(ChatProcessRequest chatProcessRequest);
}
