package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

/**
 * @ClassName ChatDrawAppVO
 * @Description 用户画图信息
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:05
 * @Version 1.0
 */
@Data
@TableName("`t_chat_draw_app`")
public class ChatDrawAppVO extends BaseEntity {
    private String mode;
    private String config;
    private String prompt;
    private Integer status;
    @TableField("user_id")
    private Long userId;
}
