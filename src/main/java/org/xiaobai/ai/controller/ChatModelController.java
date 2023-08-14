package org.xiaobai.ai.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.service.ChatModelService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ChatModelController
 * @Description 模型
 * @Author dingfeng.xiao
 * @Date 2023/7/1 15:45
 * @Version 1.0
 */
@Api(value = "模型", tags = {"模型"})
@CrossOrigin
@RestController
@RequestMapping("/chat/model")
@Validated
public class ChatModelController {
    @Resource
    private ChatModelService chatModelService;

    /**
     * 所有模型列表
     */
    @ApiOperation(value = "所有模型列表")
    @PostMapping("/list")
    public Result<List<ChatModelVO>> list() {
        return Result.success(chatModelService.list());
    }

    /**
     * 分类获取模型列表
     */
    @ApiOperation(value = "分类获取模型列表")
    @PostMapping("/listByCate")
    public Result<List<ChatModelVO>> listByCate(@NotNull Long cateId) {
        return Result.success(chatModelService.listByCate(cateId));
    }

    /**
     * 分类获取模型列表
     */
    @ApiOperation(value = "分类分组获取模型列表")
    @PostMapping("/listGroupByCate")
    public Result<Map<String,List<ChatModelVO>>> listGroupByCate() {
        return Result.success(chatModelService.listGroupByCate());
    }

    /**
     * 模型详情
     */
    @ApiOperation(value = "模型详情")
    @PostMapping("/get")
    public Result<ChatModelVO> get(@NotNull Long id) {
        return Result.success(chatModelService.getById(id));
    }
}
