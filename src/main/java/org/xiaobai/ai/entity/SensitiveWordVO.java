package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.ai.api.enums.EnableDisableStatusEnum;
import org.xiaobai.common.entity.BaseEntity;

/**
 * 敏感词表实体类
 */
@Data
@TableName("t_sensitive_word")
public class SensitiveWordVO extends BaseEntity {

    /**
     * 敏感词内容
     */
    private String word;

    /**
     * 状态 1 启用 2 停用
     */
    private EnableDisableStatusEnum status;

    /**
     * 是否删除 0 否 NULL 是
     */
    @TableLogic(value = "0", delval = "NULL")
    private Integer isDeleted;
}
