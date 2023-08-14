package org.xiaobai.ai.controller;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatModelInitWordsVO;
import org.xiaobai.ai.service.ChatModelInitWordsService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName ChatModelInitWordsController
 * @Description 模型初始化提示prompts
 * @Author dingfeng.xiao
 * @Date 2023/7/1 15:45
 * @Version 1.0
 */
@Api(value = "模型初始化提示prompts", tags = {"模型初始化提示prompts"})
@CrossOrigin
@RestController
@RequestMapping("/chat/model/initWords")
@Validated
public class ChatModelInitWordsController {
    @Resource
    private ChatModelInitWordsService chatModelInitWordsService;

    /**
     * 模型获取初始化提示prompts
     */
    @PostMapping("/listByModel")
    public Result<List<ChatModelInitWordsVO>> listByModel(@NotNull Long modelId) {
        return Result.success(chatModelInitWordsService.listByModel(modelId));
    }
}
