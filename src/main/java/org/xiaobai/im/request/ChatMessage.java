package org.xiaobai.im.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.tool.po.TImagesPO;

@Data
public class ChatMessage {
    private String type;
    private String fromUserId;
    private String toUserId;
    private Message message;

    private SysUserVO user;
    private String ipAddress;
    private String ipSource;

    @Builder
    @Data
    @AllArgsConstructor
    public static
    class Message {
        private String type;
        private String content;
        private TImagesPO file;
    }
}
