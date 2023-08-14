package org.xiaobai.ai.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 新增用户会话信息
 */
@Data
public class ChatUserChatAddRequest {
    @NotNull(message = "模型ID不能为空")
    private Long modelId;
    private String title;
    private String description;
    private Integer hisNum = 10;
}
