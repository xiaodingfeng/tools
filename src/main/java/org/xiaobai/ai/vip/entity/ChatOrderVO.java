package org.xiaobai.ai.vip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.core.enums.OrderStatusEnum;
import org.xiaobai.core.enums.PaymentMethodEnum;

import java.util.Date;

/**
 * @ClassName ChatOrderVO
 * @Description TODO
 * @Author dingfeng.xiao
 * @Date 2023/7/10 13:29
 * @Version 1.0
 */
@TableName("t_chat_order")
@Data
public class ChatOrderVO extends BaseId {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date orderDate;
    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;
    /**
     * 商品ID
     */
    private Long vipPriceId;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 商品价格
     */
    private Integer price;
    /**
     * 总金额
     */
    private Integer totalAmount;
    /**
     * 支付方式
     */
    private PaymentMethodEnum paymentMethod;
    /**
     * 备注
     */
    private String notes;
    /**
     * 取消原因
     */
    private String cancellationReason;
    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date completionDate;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastUpdated;
}
