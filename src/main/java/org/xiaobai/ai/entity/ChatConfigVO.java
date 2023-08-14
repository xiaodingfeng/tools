package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.core.enums.ValidEnum;

/**
 * chat 配置
 */
@TableName("`t_chat_config`")
@Data
public class ChatConfigVO extends BaseId {
    private String configKey;
    private String configName;
    private String configValue;
    private ValidEnum valid;
}
