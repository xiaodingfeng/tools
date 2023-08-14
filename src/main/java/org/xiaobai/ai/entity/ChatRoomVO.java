package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.common.entity.BaseEntity;

/**
 * 聊天室表实体类
 */
@Data
@TableName("t_chat_room")
public class ChatRoomVO extends BaseEntity {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 对话 id
     * 唯一
     */
    private String conversationId;

    /**
     * ip
     */
    private String ip;

    /**
     * 第一条消息主键
     * 唯一
     */
    private Long firstChatMessageId;

    /**
     * 第一条消息 id
     * 唯一
     */
    private String firstMessageId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 对话描述
     */
    private String description;

    /**
     * API 类型
     * 不同类型的对话不能一起存储
     */
    private ApiTypeEnum apiType;

    private Long modelId;

    /**
     * 是否被隐藏
     */
    private Boolean isHide;

    private Integer hisNum = 10;
}
