package org.xiaobai.ai.api.listener;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.api.domain.vo.ChatReplyMessageVO;

@Service
@Slf4j
public class StreamCompleteListener extends AbstractStreamListener {


    @Override
    public void onMessage(String newMessage, String receivedMessage, ChatReplyMessageVO chatReplyMessageVO, int messageCount) {

    }

    @Override
    public void onError(String receivedMessage, Throwable t, String responseStr, @Nullable Response response) {

    }

    /**
     * 结束响应
     *
     * @param receivedMessage 接收到消息
     */
    public void onComplete(String receivedMessage) {
        log.info("消息发送结束，消息内容：{}", receivedMessage);
    }
}
