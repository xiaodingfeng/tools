package org.xiaobai.ai.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatCategoryVO;
import org.xiaobai.ai.service.ChatCategoryService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ChatCategoryController
 * @Description 分类
 * @Author dingfeng.xiao
 * @Date 2023/7/1 15:45
 * @Version 1.0
 */
@Api(value = "分类", tags = {"分类"})
@CrossOrigin
@RestController
@RequestMapping("/chat/category")
@Validated
public class ChatCategoryController {
    @Resource
    private ChatCategoryService chatCategoryService;

    /**
     * 模型分类
     */
    @ApiOperation(value = "模型分类")
    @PostMapping("/list")
    public Result<List<ChatCategoryVO>> list() {
        return Result.success(chatCategoryService.list());
    }
}
