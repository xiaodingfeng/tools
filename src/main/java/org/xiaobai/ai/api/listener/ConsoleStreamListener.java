package org.xiaobai.ai.api.listener;

import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.xiaobai.ai.api.domain.vo.ChatReplyMessageVO;

/**
 * 控制台消息流监听
 */
public class ConsoleStreamListener extends AbstractStreamListener {

    @Override
    public void onMessage(String newMessage, String receivedMessage, ChatReplyMessageVO chatReplyMessageVO, int i) {
        System.out.println(newMessage);
    }

    @Override
    public void onError(String receivedMessage, Throwable t, String responseStr, @Nullable Response response) {
        System.out.println("控制台消息输出异常了");
    }
}
