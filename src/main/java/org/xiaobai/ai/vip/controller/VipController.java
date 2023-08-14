package org.xiaobai.ai.vip.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.vip.entity.ChatVipVO;
import org.xiaobai.ai.vip.response.UserVipInfoResponse;
import org.xiaobai.ai.vip.service.ChatVipService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName VipController
 * @Description TODO
 * @Author dingfeng.xiao
 * @Date 2023/7/11 13:21
 * @Version 1.0
 */
@Api("会员信息")
@Slf4j
@RequestMapping("/chat/vip")
@RestController
public class VipController {
    @Resource
    private ChatVipService chatVipService;

    /**
     * 会员价格
     */
    @ApiOperation(value = "会员价格")
    @PostMapping("/price")
    public Result<List<ChatVipVO>> price() throws IOException {
        return Result.success(chatVipService.listValidVip());
    }

    /**
     * 用户会员信息
     */
    @ApiOperation(value = "用户会员信息")
    @PostMapping("/my")
    public Result<UserVipInfoResponse> userVipInfo() {
        return Result.success(chatVipService.userVipInfo(UserUtil.getUserId()));
    }

    /**
     * 会员对比表格
     */
    @ApiOperation(value = "会员对比表格")
    @PostMapping("/vipTable")
    public Result<List<Map<String, Object>>> vipTable() {
        return Result.success(chatVipService.vipTable());
    }
}
