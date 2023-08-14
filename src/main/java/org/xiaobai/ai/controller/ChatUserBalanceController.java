package org.xiaobai.ai.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatUserBalanceVO;
import org.xiaobai.ai.response.DayBalanceResponse;
import org.xiaobai.ai.response.RoomInitResponse;
import org.xiaobai.ai.service.ChatUserBalanceService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Api(value = "用户额度管理", tags = {"用户额度管理"})
@CrossOrigin
@RestController
@RequestMapping("/chat/user/balance")
@Validated
public class ChatUserBalanceController {

    @Resource
    private ChatUserBalanceService chatUserBalanceService;
    /**
     * 额度信息
     */
    @ApiOperation(value = "额度信息")
    @PostMapping("/getBalance")
    public Result<ChatUserBalanceVO> getBalance() {
        return Result.success(chatUserBalanceService.getBalance(UserUtil.getUserId()));
    }
    /**
     * 每日可以领取的额度
     */
    @ApiOperation(value = "每日可以领取的额度")
    @PostMapping("/dayBalance")
    public Result<DayBalanceResponse> dayBalance() {
        return Result.success(chatUserBalanceService.dayBalance(UserUtil.getUserId()));
    }
    /**
     * 领取额度
     */
    @ApiOperation(value = "领取额度")
    @PostMapping("/receiveBalance")
    public Result<Boolean> receiveBalance() {
        return Result.success(chatUserBalanceService.receiveBalance(UserUtil.getUserId()));
    }
}
