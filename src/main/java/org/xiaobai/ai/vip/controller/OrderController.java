package org.xiaobai.ai.vip.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.request.ChatVipOrderAddRequest;
import org.xiaobai.ai.vip.response.ChatVipOrderAddResponse;
import org.xiaobai.ai.vip.service.ChatOrderService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.enums.OrderStatusEnum;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName OrderController
 * @Description TODO
 * @Author dingfeng.xiao
 * @Date 2023/7/10 14:46
 * @Version 1.0
 */
@Api("订单信息")
@Slf4j
@RequestMapping("/chat/order")
@RestController
public class OrderController {

    @Resource
    private ChatOrderService chatOderService;

    /**
     * 创建订单
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/addVipOrder")
    public Result<ChatVipOrderAddResponse> addVipOrder(@Valid @RequestBody ChatVipOrderAddRequest addRequest) throws IOException {
        return Result.success(chatOderService.addVipOrder(addRequest));
    }

    /**
     * 订单明细
     */
    @ApiOperation(value = "订单明细")
    @PostMapping("/detail")
    public Result<ChatOrderVO> detail(@NotNull(message = "订单ID不能为空") Long orderId) throws IOException {
        return Result.success(chatOderService.getById(orderId));
    }

    /**
     * 订单列表
     */
    @ApiOperation(value = "订单列表")
    @PostMapping("/my")
    public Result<List<ChatOrderVO>> my() throws IOException {
        return Result.success(chatOderService.list(new LambdaQueryWrapper<ChatOrderVO>()
        .eq(ChatOrderVO::getUserId, UserUtil.getUserId())));
    }

    /**
     * 支付状态
     */
    @ApiOperation(value = "支付状态")
    @PostMapping("/payresult")
    public Result<OrderStatusEnum> payResult(@NotNull(message = "订单ID不能为空") Long orderId) throws IOException {
        return Result.success(chatOderService.payResult(orderId));
    }

    @ApiOperation(value = "扫码支付回调通知处理")
    @PostMapping("/notify/scanpay")
    public String notifyScanPay(String xmlData) throws WxPayException {
        return chatOderService.notifyScanPay(xmlData);
    }

    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/paynotify")
    public String payNotify(String xmlData) throws WxPayException {
        return chatOderService.payNotify(xmlData);
    }
}
