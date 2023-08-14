package org.xiaobai.ai.vip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaobai.core.enums.DateTypeEnum;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.core.enums.ValidEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ClassName ChatVipPrice
 * @Description 会员价格
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:57
 * @Version 1.0
 */
@Data
@TableName("t_chat_vip_price")
public class ChatVipPriceVO extends BaseId {
    private Long vipId;
    private String title;
    /**
     * 日期类型
     */
    private DateTypeEnum dateType;
    /**
     * 日期数量，一个月，一周等等
     */
    private Integer dateCount;
    private String description;
    /**
     * 原价
     */
    private Integer oldPrice;
    /**
     * 现价
     */
    private Integer price;
    /**
     * 排序
     */
    private Integer sort;

    private ValidEnum valid;

    /**
     * 优惠百分比
     */
    @TableField(exist = false)
    private String preferential;

    public String getPreferential() {
        BigDecimal a = new BigDecimal(oldPrice);
        BigDecimal b = new BigDecimal(price);
        BigDecimal percentage = a.subtract(b).multiply(BigDecimal.valueOf(100)
                .divide(a, 2, RoundingMode.HALF_UP));
        return percentage + "%";
    }
}
