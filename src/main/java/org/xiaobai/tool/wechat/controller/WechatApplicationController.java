package org.xiaobai.tool.wechat.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.tool.response.Result;
import org.xiaobai.tool.wechat.WeChatApplicationService;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/bspApp")
public class WechatApplicationController {
    @Resource
    private WeChatApplicationService bspAppService;

    @ApiOperation("蜡笔小新同步")
    @RequestMapping("/sync")
    public Result<String> sync() throws IOException {
        bspAppService.downImage();
        return Result.success();
    }
}
