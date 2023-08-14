package org.xiaobai.ai.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatUserChatSendChatRequest {
    @NotNull(message = "会话ID不能为空")
    private Long userChatId;
    @NotBlank(message = "会话内容不能为空")
    private String content;
}
