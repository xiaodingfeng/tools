package org.xiaobai.ai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.request.ChatDrawAppAddTaskRequest;
import org.xiaobai.ai.request.ChatDrawAppMyPageRequest;
import org.xiaobai.ai.response.ChatDrawAppMyPageResponse;
import org.xiaobai.ai.service.ChatDrawAppService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName DrawAppController
 * @Description 画图
 * @Author dingfeng.xiao
 * @Date 2023/6/30 15:45
 * @Version 1.0
 */
@Api(value = "画图", tags = {"画图"})
@CrossOrigin
@RestController
@RequestMapping("/chat/drawApp")
@Validated
public class ChatDrawAppController {

    @Resource
    private ChatDrawAppService chatDrawAppService;

    /**
     * 用户画图信息列表
     */
    @ApiOperation(value = "用户画图信息列表")
    @PostMapping("/my/list")
    public Result<Page<ChatDrawAppMyPageResponse>> myListPage(@RequestBody ChatDrawAppMyPageRequest drawAppMyPageRequest) {
        return Result.success(chatDrawAppService.myListPage(UserUtil.getUserId(), drawAppMyPageRequest));
    }

    /**
     * 用户画图新增任务
     */
    @ApiOperation(value = "用户画图新增任务")
    @PostMapping("/my/addTask")
    public ResponseBodyEmitter addTask(@RequestBody ChatDrawAppAddTaskRequest drawAppAddTaskRequest) throws IOException {
        return chatDrawAppService.addTask(UserUtil.getUserId(), drawAppAddTaskRequest);
    }
}
