package org.xiaobai.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName PaymentMethodEnum
 * @Description 支付方式
 * @Author dingfeng.xiao
 * @Date 2023/7/10 14:07
 * @Version 1.0
 */
@AllArgsConstructor
public enum PaymentMethodEnum {
    OWN("OWN", "本系统"),
    CHAT_PAY("CHAT_PAY", "微信"),
    ALIPAY("ALIPAY", "支付宝");
    @Getter
    @EnumValue
    private final String type;

    @Getter
    @JsonValue
    private final String message;
}
