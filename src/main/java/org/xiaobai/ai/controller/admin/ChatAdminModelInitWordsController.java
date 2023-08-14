package org.xiaobai.ai.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.ai.entity.ChatModelInitWordsVO;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.request.ChatModelInitWordsAddRequest;
import org.xiaobai.ai.request.ChatModelInitWordsUpdateRequest;
import org.xiaobai.ai.request.query.ChatModelInitWordsPageQuery;
import org.xiaobai.ai.service.ChatModelInitWordsService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
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
@RequestMapping("/chat/admin/model/initWords")
@Validated
public class ChatAdminModelInitWordsController {
    @Resource
    private ChatModelInitWordsService chatModelInitWordsService;

    /**
     * 模型获取初始化提示prompts
     */
    @PostMapping("/listByModel")
    public Result<List<ChatModelInitWordsVO>> listByModel(@NotNull Long modelId) {
        return Result.success(chatModelInitWordsService.listByModel(modelId));
    }

    /**
     * 初始化提示prompts分页列表
     */
    @ApiOperation(value = "初始化提示prompts分页列表")
    @PostMapping("/page")
    public Result<IPage<ChatModelInitWordsVO>> pageInitWords(@Valid @RequestBody ChatModelInitWordsPageQuery query) {
        return Result.success(chatModelInitWordsService.pageInitWords(query));
    }

    /**
     * 模型初始化提示新增
     */
    @ApiOperation(value = "模型初始化提示新增")
    @PostMapping("/add")
    public Result<Long> add(@RequestBody ChatModelInitWordsAddRequest addRequest) {
        ChatModelInitWordsVO chatModelInitWordsVO = new ChatModelInitWordsVO();
        BeanUtils.copyProperties(addRequest, chatModelInitWordsVO);
        chatModelInitWordsService.save(chatModelInitWordsVO);
        return Result.success(chatModelInitWordsVO.getId());
    }

    /**
     * 模型初始化提示修改
     */
    @ApiOperation(value = "模型初始化提示修改")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody ChatModelInitWordsUpdateRequest updateRequest) {
        ChatModelInitWordsVO chatModelInitWordsVO = new ChatModelInitWordsVO();
        BeanUtils.copyProperties(updateRequest, chatModelInitWordsVO);
        return Result.success(chatModelInitWordsService.updateById(chatModelInitWordsVO));
    }

    /**
     * 模型初始化提示详情
     */
    @ApiOperation(value = "模型初始化提示详情")
    @PostMapping("/get")
    public Result<ChatModelInitWordsVO> get(@NotNull Long id) {
        return Result.success(chatModelInitWordsService.getById(id));
    }

    /**
     * 模型初始化提示删除
     */
    @ApiOperation(value = "模型初始化提示删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(@NotNull Long id) {
        return Result.success(chatModelInitWordsService.removeById(id));
    }
}
