package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

/**
 * 分类
 */
@TableName("`t_chat_category`")
@Data
public class ChatCategoryVO extends BaseEntity {
    private String name;
    private String description;
    private String ico;
    private Integer sort;
}
