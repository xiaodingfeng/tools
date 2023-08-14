package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

/**
 * @ClassName ChatDrawPictureVO
 * @Description 图片明细
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:06
 * @Version 1.0
 */
@Data
@TableName("`t_chat_draw_picture`")
public class ChatDrawPictureVO extends BaseEntity {
    @TableField("draw_app_id")
    private Long drawAppId;
    private String name;
    private String result;
    private String prompt;
}
