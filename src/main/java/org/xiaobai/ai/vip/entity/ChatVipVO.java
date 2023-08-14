package org.xiaobai.ai.vip.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.common.entity.BaseEntity;
import org.xiaobai.core.enums.DateTypeEnum;
import org.xiaobai.core.enums.ValidEnum;

import java.util.List;

/**
 * @ClassName ChatVipVO
 * @Description 会员信息
 * @Author dingfeng.xiao
 * @Date 2023/7/10 9:58
 * @Version 1.0
 */
@Data
@TableName("t_chat_vip")
public class ChatVipVO extends BaseEntity {
    private String name;
    private String description;
    private String options;
    /**
     * 是否生效 1生效 0未生效
     */
    private ValidEnum valid;

    private Integer sort;

    @TableField(exist = false)
    private List<ChatVipPriceVO> prices;

    @Data
    public static class Options {
        /**
         * chat数量单位 每天10次，每月10次
         */
        private DateTypeEnum dateType;
        /**
         * chat额度
         */
        private Integer receiveBalance;
    }

    public Options getOptions() {
        return JSON.parseObject(options, Options.class);
    }
}
