package org.xiaobai.ai.vip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.core.enums.DateTypeEnum;
import org.xiaobai.core.enums.ValidEnum;

/**
 * @ClassName ChatFreeConfigVO
 * @Description 用户免费额度配置
 * @Author dingfeng.xiao
 * @Date 2023/7/11 16:33
 * @Version 1.0
 */
@Data
@TableName("t_chat_free_config")
public class ChatFreeConfigVO extends BaseId {
    /**
     * 免费领取额度
     */
    private Integer receiveBalance;
    /**
     * 日期类型，比如一天最大10条额度
     */
    private DateTypeEnum dateType;
    /**
     * 是否有效
     */
    private ValidEnum valid;
}
