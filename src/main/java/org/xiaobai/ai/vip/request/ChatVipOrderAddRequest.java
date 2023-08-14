package org.xiaobai.ai.vip.request;

import lombok.Data;
import org.xiaobai.core.enums.PaymentMethodEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ChatVipOrderAddRequest
 * @Description 用户会员新增请求
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:16
 * @Version 1.0
 */
@Data
public class ChatVipOrderAddRequest {
    @NotNull(message = "会员价格信息不能为空")
    private Long vipPriceId;
    private Integer count = 1;
    private String paymentMethod;
}
