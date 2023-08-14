package org.xiaobai.ai.vip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.request.ChatVipOrderAddRequest;
import org.xiaobai.ai.vip.response.ChatVipOrderAddResponse;
import org.xiaobai.core.enums.OrderStatusEnum;

import java.io.IOException;

/**
 * @ClassName ChatOderService
 * @Description 订单
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:06
 * @Version 1.0
 */
public interface ChatOrderService extends IService<ChatOrderVO> {
    /**
     * 创建vip订单信息，并返回二维码
     * @param addRequest
     */
    ChatVipOrderAddResponse addVipOrder(ChatVipOrderAddRequest addRequest) throws IOException;
    /**
     * 订单支付结果
     * @param orderId
     */
    OrderStatusEnum payResult(Long orderId) throws IOException;

    /**
     * 扫码支付回调
     * @param xmlData
     * @return
     */
   String notifyScanPay(String xmlData) throws WxPayException;

    /**
     * 支付回调
     * @param xmlData
     * @return
     */
    String payNotify(String xmlData) throws WxPayException;

    /**
     * 更新订单状态
     * @param orderId
     * @param statusEnum
     */
   Boolean updateStatus(Long orderId, OrderStatusEnum statusEnum);
}
