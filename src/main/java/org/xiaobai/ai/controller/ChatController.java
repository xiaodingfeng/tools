package org.xiaobai.ai.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.utils.WebUtil;
import org.xiaobai.tool.response.Result;
import org.xiaobai.tool.service.ApiService;

import javax.annotation.Resource;

/**
 * @ClassName ChatController
 * @Description chat接口
 * @Author dingfeng.xiao
 * @Date 2023/6/30 9:42
 * @Version 1.0
 */
@Api(value = "chat接口", tags = {"chat接口"})
@CrossOrigin
@RestController
@RequestMapping("/chat")
@Validated
public class ChatController {
    @Resource
    private ApiService apiService;
    /**
     * 用户反馈
     */
    @ApiOperation(value = "用户反馈")
    @PostMapping("/errorBack")
    public Result<Boolean> errorBack() throws Exception {
        apiService.sendMail("1440651649@qq.com", "用户反馈信息", UserUtil.getUserId() + "用户点击了用户反馈, ip:" + WebUtil.getIp());
        return Result.success(true);
    }
}
