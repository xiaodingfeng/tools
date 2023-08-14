package org.xiaobai.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

/**
 * @ClassName ChatDrawPictureRealVO
 * @Description 真实图片地址
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:08
 * @Version 1.0
 */
@Data
@TableName("`t_chat_draw_picture_real`")
public class ChatDrawPictureRealVO extends BaseEntity {
    @TableField("draw_picture_id")
    private String drawPictureId;
    private String result;
}
