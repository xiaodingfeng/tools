package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

/**
 * 模型
 */
@Data
@TableName("`t_chat_model`")
public class ChatModelVO extends BaseEntity {
    @TableField("cate_id")
    private Long cateId;
    @TableField(exist = false)
    private String cateName;
    private String title;
    private String ico;
    private String description;
    @TableField("system_prompts")
    private String systemPrompts;
    private Integer sort;
}
