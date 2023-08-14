package org.xiaobai.ai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatConfigVO;
import org.xiaobai.ai.service.ChatConfigService;
import org.xiaobai.core.enums.ValidEnum;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "chat配置", tags = {"chat配置"})
@CrossOrigin
@RestController
@RequestMapping("/chat/config")
@Validated
public class ChatConfigController {
    @Resource
    private ChatConfigService chatConfigService;
    /**
     * 配置列表
     */
    @ApiOperation(value = "配置列表")
    @PostMapping("/list")
    public Result<List<ChatConfigVO>> list() {
        return Result.success(chatConfigService.list(new LambdaQueryWrapper<ChatConfigVO>().eq(ChatConfigVO::getValid, ValidEnum.VALID)));
    }

    /**
     * 获取一次chat 消耗额度
     */
    @ApiOperation(value = "获取一次chat 消耗额度")
    @PostMapping("/findChatBalance")
    public Result<Integer> findChatBalance() {
        return Result.success(chatConfigService.findChatBalance());
    }
}
