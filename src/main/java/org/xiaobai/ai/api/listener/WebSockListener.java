package org.xiaobai.ai.api.listener;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.api.domain.vo.ChatReplyMessageVO;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @ClassName WebSockListener
 * @Author dingfeng.xiao
 * @Date 2023/8/4 13:36
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
public class WebSockListener  extends AbstractStreamListener{

    private final ChatProcessRequest chatProcessRequest;

    @Override
    public void onMessage(String newMessage, String receivedMessage, ChatReplyMessageVO chatReplyMessageVO, int messageCount) {

    }

    @Override
    public void onError(String receivedMessage, Throwable t, String responseStr, @Nullable Response response) {
        this.finishConsumer("消息处理异常," + responseStr);
    }

    /**
     * 结束响应
     *
     * @param receivedMessage 接收到消息
     */
    public void onComplete(String receivedMessage) {
        this.finishConsumer(receivedMessage);
    }

    private void finishConsumer(String receivedMessage) {
        for (Consumer<String> consumer : chatProcessRequest.getSocketFinishConsumer()) {
            try {
                consumer.accept(receivedMessage);
            } catch (Exception e) {
                log.error("结束响应，Consumer ，error: {}", e.getMessage());
            }
        }
    }
}
