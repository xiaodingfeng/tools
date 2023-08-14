package org.xiaobai.ai.vip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;

import java.util.Date;

/**
 * @ClassName ChatVipUserVO
 * @Description 会员用户信息
 * @Author dingfeng.xiao
 * @Date 2023/7/10 9:59
 * @Version 1.0
 */
@Data
@TableName("t_chat_user_vip")
public class ChatVipUserVO extends BaseEntity {
    /**
     * vipid
     */
    private Long vipId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date endTime;

    @TableField(exist = false)
    private ChatVipVO vipInfo;
}
