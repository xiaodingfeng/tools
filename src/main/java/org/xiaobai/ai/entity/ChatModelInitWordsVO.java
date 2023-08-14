package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

/**
 * 模型初始化信息
 */
@Data
@TableName("`t_chat_model_init_words`")
public class ChatModelInitWordsVO extends BaseEntity {
    @TableField("model_id")
    private Long modelId;
    private String prompts;
}
