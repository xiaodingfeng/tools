package org.xiaobai.ai.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.request.ChatModelAddRequest;
import org.xiaobai.ai.request.ChatModelUpdateRequest;
import org.xiaobai.ai.request.query.ChatModelPageQuery;
import org.xiaobai.ai.service.ChatModelService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
@RequestMapping("/chat/admin/model")
@Validated
public class ChatAdminModelController {
    @Resource
    private ChatModelService chatModelService;

    /**
     * 分类获取模型列表
     */
    @ApiOperation(value = "分类获取模型列表")
    @PostMapping("/listByCate")
    public Result<List<ChatModelVO>> listByCate(@NotNull Long cateId) {
        return Result.success(chatModelService.listByCate(cateId));
    }

    /**
     * 模型分页列表
     */
    @ApiOperation(value = "模型分页列表")
    @PostMapping("/page")
    public Result<IPage<ChatModelVO>> pageModel(@Valid @RequestBody ChatModelPageQuery query) {
        return Result.success(chatModelService.pageModel(query));
    }


    /**
     * 模型新增
     */
    @ApiOperation(value = "模型新增")
    @PostMapping("/add")
    public Result<Long> add(@Valid @RequestBody ChatModelAddRequest addRequest) {
        ChatModelVO chatModelVO = new ChatModelVO();
        BeanUtils.copyProperties(addRequest, chatModelVO);
        chatModelService.save(chatModelVO);
        return Result.success(chatModelVO.getId());
    }

    /**
     * 模型修改
     */
    @ApiOperation(value = "模型修改")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody ChatModelUpdateRequest updateRequest) {
        ChatModelVO chatModelVO = new ChatModelVO();
        BeanUtils.copyProperties(updateRequest, chatModelVO);
        return Result.success(chatModelService.updateById(chatModelVO));
    }

    /**
     * 模型详情
     */
    @ApiOperation(value = "模型详情")
    @PostMapping("/get")
    public Result<ChatModelVO> get(@NotNull Long id) {
        return Result.success(chatModelService.getById(id));
    }

    /**
     * 模型删除
     */
    @ApiOperation(value = "模型删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(@NotNull Long id) {
        return Result.success(chatModelService.removeById(id));
    }
}
