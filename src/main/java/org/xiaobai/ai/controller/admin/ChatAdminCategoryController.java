package org.xiaobai.ai.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.ai.entity.ChatCategoryVO;
import org.xiaobai.ai.request.ChatCategoryAddRequest;
import org.xiaobai.ai.request.ChatCategoryUpdateRequest;
import org.xiaobai.ai.request.query.ChatCategoryPageQuery;
import org.xiaobai.ai.service.ChatCategoryService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ChatAdminCategoryController
 * @Description 分类
 * @Author dingfeng.xiao
 * @Date 2023/7/1 15:45
 * @Version 1.0
 */
@Api(value = "分类", tags = {"分类"})
@CrossOrigin
@RestController
@RequestMapping("/chat/admin/category")
@Validated
public class ChatAdminCategoryController {
    @Resource
    private ChatCategoryService chatCategoryService;

    /**
     * 模型分类
     */
    @ApiOperation(value = "模型分类")
    @PostMapping("/page")
    public Result<IPage<ChatCategoryVO>> pageCategory(@Valid @RequestBody ChatCategoryPageQuery query) {
        return Result.success(chatCategoryService.pageCategory(query));
    }

    /**
     * 模型分类新增
     */
    @ApiOperation(value = "模型分类新增")
    @PostMapping("/add")
    public Result<Long> add(@RequestBody @Valid ChatCategoryAddRequest addRequest) {
        ChatCategoryVO categoryVO = new ChatCategoryVO();
        BeanUtils.copyProperties(addRequest, categoryVO);
        chatCategoryService.save(categoryVO);
        return Result.success(categoryVO.getId());
    }

    /**
     * 模型分类修改
     */
    @ApiOperation(value = "模型分类修改")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid ChatCategoryUpdateRequest updateRequest) {
        ChatCategoryVO categoryVO = new ChatCategoryVO();
        BeanUtils.copyProperties(updateRequest, categoryVO);
        return Result.success(chatCategoryService.updateById(categoryVO));
    }

    /**
     * 模型分类详情
     */
    @ApiOperation(value = "模型分类详情")
    @PostMapping("/get")
    public Result<ChatCategoryVO> get(@NotNull Long id) {
        return Result.success(chatCategoryService.getById(id));
    }

    /**
     * 模型分类删除
     */
    @ApiOperation(value = "模型分类删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(@NotNull Long id) {
        return Result.success(chatCategoryService.removeById(id));
    }
}
