package org.xiaobai.ai.api.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 消息处理请求
 */
@Data
public class ChatProcessRequest {

    @NotBlank(message = "内容不能为空")
    @Size(min = 1, max = 10000, message = "问题字数范围[1, 10000]")
    private String prompt;

    @NotNull(message = "配置不能为空")
    private Options options;

    private String systemMessage;

    private Long userId;

    /**
     * 消息完成后执行的方法
     */
    private List<Consumer<String>> socketFinishConsumer = new ArrayList<>();

    public void addFinishConsumer(Consumer<String> consumer) {
        socketFinishConsumer.add(consumer);
    }

    @Data
    public static class Options {
        @NotNull(message = "会话ID不能为空")
        private Long roomId;

        private String conversationId;

        /**
         * 这里的父级消息指的是回答的父级消息 id
         * 前端发送问题，需要上下文的话传回答的父级消息 id
         */
        private String parentMessageId;
    }
}
