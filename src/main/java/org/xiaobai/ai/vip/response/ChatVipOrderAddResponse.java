package org.xiaobai.ai.vip.response;

import lombok.Data;

/**
 * @ClassName ChatVipOrderAddResponse
 * @Description 创建订单响应
 * @Author dingfeng.xiao
 * @Date 2023/7/11 10:57
 * @Version 1.0
 */
@Data
public class ChatVipOrderAddResponse {
    private String codeUrlBase64;
    private Long orderId;
}
