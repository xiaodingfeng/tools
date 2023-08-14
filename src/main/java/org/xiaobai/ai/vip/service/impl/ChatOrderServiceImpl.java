package org.xiaobai.ai.vip.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxScanPayNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.entity.ChatVipPriceVO;
import org.xiaobai.ai.vip.mapper.ChatOrderMapper;
import org.xiaobai.ai.vip.queue.OrderTimeoutQueue;
import org.xiaobai.ai.vip.request.ChatVipOrderAddRequest;
import org.xiaobai.ai.vip.response.ChatVipOrderAddResponse;
import org.xiaobai.ai.vip.service.ChatOrderService;
import org.xiaobai.ai.vip.service.ChatVipPriceService;
import org.xiaobai.ai.vip.service.ChatVipUserService;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.config.WxPayProperties;
import org.xiaobai.core.enums.DateTypeEnum;
import org.xiaobai.core.enums.OrderStatusEnum;
import org.xiaobai.core.enums.PaymentMethodEnum;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.BufferedImageUtil;
import org.xiaobai.core.utils.DateTimeUtil;
import org.xiaobai.core.utils.WebUtil;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName ChatOrderServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:07
 * @Version 1.0
 */
@Slf4j
@Service
public class ChatOrderServiceImpl extends ServiceImpl<ChatOrderMapper, ChatOrderVO> implements ChatOrderService {

    @Resource
    private ChatVipPriceService chatVipPriceService;
    @Resource
    private WxPayService wxPayService;
    @Resource
    private ChatVipUserService chatVipUserService;
    @Resource
    @Lazy
    private OrderTimeoutQueue orderTimeoutQueue;
    @Resource
    private WxPayProperties wxPayProperties;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChatVipOrderAddResponse addVipOrder(ChatVipOrderAddRequest addRequest) throws IOException {
        log.info("用户提交会员申请： param：{}", addRequest);
        Long vipPriceId = addRequest.getVipPriceId();
        ChatVipPriceVO vipPrice = chatVipPriceService.getValidVipPrice(vipPriceId);
        if (Objects.isNull(vipPrice)) {
            throw new TipException("会员价格信息有误");
        }
        ChatOrderVO orderVO = assembleOrderVO(addRequest, vipPrice);
        orderVO.setId(IdWorker.getId());
        save(orderVO);
        log.info("创建订单： 金额： {}， vo：{}", orderVO.getTotalAmount(), orderVO);
        ChatVipOrderAddResponse response =  new ChatVipOrderAddResponse();
        response.setOrderId(orderVO.getId());
        switch (addRequest.getPaymentMethod()) {
            case "CHAT_PAY": {
                String wxPayUrl = createWxPayUrl(orderVO);
                BufferedImage bufferedImage = QrCodeUtil.generate(wxPayUrl, 400, 400);
                String base64 = BufferedImageUtil.bufferedImageToBase64(bufferedImage);
                payingOrder(orderVO);
                orderTimeoutQueue.enqueueOrderTimeout(String.valueOf(orderVO.getId()));
                response.setCodeUrlBase64(base64);
            }
            case "ALIPAY": {
                // TODO 支付宝支付实现
                orderTimeoutQueue.enqueueOrderTimeout(String.valueOf(orderVO.getId()));
                completeOrder(orderVO);
                response.setCodeUrlBase64(null);
            }
            default: {
                // TODO 站点支付实现
                orderTimeoutQueue.enqueueOrderTimeout(String.valueOf(orderVO.getId()));
                completeOrder(orderVO);
                response.setCodeUrlBase64(null);
            }
        }
        return response;
    }

    private String createWxPayUrl(ChatOrderVO orderVO) {
        WxPayUnifiedOrderRequest unifiedOrderRequest = new WxPayUnifiedOrderRequest();
        unifiedOrderRequest.setBody("AI-会员充值");
        unifiedOrderRequest.setOutTradeNo(String.valueOf(orderVO.getId()));
        unifiedOrderRequest.setTotalFee(orderVO.getTotalAmount());
        unifiedOrderRequest.setSpbillCreateIp(WebUtil.getIp());
        unifiedOrderRequest.setTimeExpire(DateTimeUtil.formatDate(DateTimeUtil.nextTime(15, DateTypeEnum.MINUTE), "yyyyMMddHHmmss"));
        unifiedOrderRequest.setNotifyUrl(wxPayProperties.getNotifyUrl());
        unifiedOrderRequest.setTradeType(WxPayConstants.TradeType.NATIVE);
        unifiedOrderRequest.setProductId(String.valueOf(orderVO.getVipPriceId()));
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = null;
        try {
            wxPayUnifiedOrderResult = wxPayService.unifiedOrder(unifiedOrderRequest);
        } catch (WxPayException e) {
            throw new TipException("获取支付URL异常，errorMsg： " + e.getCustomErrorMsg());
        }
        log.info("创建微信支付订单：content: {}", wxPayUnifiedOrderResult);
        return wxPayUnifiedOrderResult.getCodeURL();
    }

    /**
     * 组装orderVO
     * @param addRequest
     * @param vipPrice
     */
    private ChatOrderVO assembleOrderVO(ChatVipOrderAddRequest addRequest, ChatVipPriceVO vipPrice) {
        Date nowDate = new Date();
        ChatOrderVO orderVO = new ChatOrderVO();
        orderVO.setId(IdWorker.getId());
        orderVO.setOrderDate(nowDate);
        orderVO.setVipPriceId(vipPrice.getId());
        orderVO.setPrice(vipPrice.getPrice());
        orderVO.setQuantity(addRequest.getCount());
        orderVO.setTotalAmount(vipPrice.getPrice() * addRequest.getCount());
        orderVO.setLastUpdated(nowDate);
        orderVO.setOrderStatus(OrderStatusEnum.CREATE);
        orderVO.setPaymentMethod(PaymentMethodEnum.valueOf(addRequest.getPaymentMethod()));
        orderVO.setUserId(UserUtil.getUserId());
        return orderVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String notifyScanPay(String xmlData) {
        try {
            WxScanPayNotifyResult result = wxPayService.parseScanPayNotifyResult(xmlData);
            // 获取orderId
            String orderId = result.getProductId();
            ChatOrderVO orderVO = getById(orderId);
            if (Objects.isNull(orderVO)) {
                throw new TipException("订单记录不存在");
            }
            completeOrder(orderVO);
            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            return WxPayNotifyResponse.fail("失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String payNotify(String xmlData) {
        try {
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);
            // 获取orderId
            String orderId = result.getOutTradeNo();
            ChatOrderVO orderVO = getById(orderId);
            if (Objects.isNull(orderVO)) {
                throw new TipException("订单记录不存在");
            }
            completeOrder(orderVO);
            return WxPayNotifyResponse.success("成功");
        } catch (WxPayException e) {
            log.error("支付失败：msg：{}", xmlData);
            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            return WxPayNotifyResponse.fail("失败");
        }
    }

    @Async
    public void payingOrder(ChatOrderVO orderVO) {
        Date date = new Date();
        update(new LambdaUpdateWrapper<ChatOrderVO>()
        .eq(BaseId::getId, orderVO.getId())
        .set(ChatOrderVO::getLastUpdated, date)
        .set(ChatOrderVO::getOrderStatus, OrderStatusEnum.PAYING));
    }

    public void completeOrder(ChatOrderVO orderVO) {
        Date date = new Date();
        // 移除延时队列
        orderTimeoutQueue.dequeueOrderTimeout(String.valueOf(orderVO.getId()));
        update(new LambdaUpdateWrapper<ChatOrderVO>()
                .eq(BaseId::getId, orderVO.getId())
                .set(ChatOrderVO::getLastUpdated, date)
                .set(ChatOrderVO::getCompletionDate, date)
                .set(ChatOrderVO::getOrderStatus, OrderStatusEnum.COMPLETE));
        // 这里给用户 充值VIP
        if (!chatVipUserService.addUserVip(orderVO)) {
            throw new TipException("用户充值失败");
        }
    }

    @Override
    public Boolean updateStatus(Long orderId, OrderStatusEnum statusEnum) {
        return update(new LambdaUpdateWrapper<ChatOrderVO>()
                .eq(BaseId::getId, orderId)
                .set(ChatOrderVO::getLastUpdated, new Date())
                .set(ChatOrderVO::getOrderStatus, statusEnum));
    }

    @Override
    public OrderStatusEnum payResult(Long orderId) {
        ChatOrderVO orderVO = getById(orderId);
        if (Objects.isNull(orderVO)) {
            return null;
        }
        return orderVO.getOrderStatus();
    }
}
