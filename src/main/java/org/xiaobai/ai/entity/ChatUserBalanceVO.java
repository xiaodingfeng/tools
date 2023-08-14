package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

@Data
@TableName("t_chat_user_balance")
public class ChatUserBalanceVO extends BaseEntity {
    private Long userId;
    private Integer chatBalance = 0;
}
