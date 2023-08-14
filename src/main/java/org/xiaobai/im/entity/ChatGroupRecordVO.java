package org.xiaobai.im.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.im.request.ChatMessage;

/**
 * @ClassName ChatGroupRecordVO
 * @Description 聊天室
 * @Author dingfeng.xiao
 * @Date 2023/7/17 14:41
 * @Version 1.0
 */
@Data
@TableName("t_chat_group_record")
public class ChatGroupRecordVO extends BaseEntity {
    private Long groupId;
    private Long userId;
    private String content;
    private String ipAddress;
    private String ipSource;
    private Integer type;
    @TableField(exist = false)
    private String messageType;

    @TableField(exist = false)
    private SysUserVO user;

    @TableField(exist = false)
    private ChatMessage.Message message;

    public ChatMessage.Message getMessage() {
        return JSON.parseObject(content, ChatMessage.Message.class);
    }
}
