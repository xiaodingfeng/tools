package org.xiaobai.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName OrderStatusEnum
 * @Description 订单状态
 * @Author dingfeng.xiao
 * @Date 2023/7/10 14:07
 * @Version 1.0
 */
@AllArgsConstructor
public enum OrderStatusEnum {
    CREATE(0, "创建"),
    PAYING(1, "支付中"),
    COMPLETE(2, "支付完成"),
    CANCEL(3, "已取消"),
    TIME_OUT(4, "已超时");
    @Getter
    @EnumValue
    private final int status;

    @Getter
    @JsonValue
    private final String message;
}
