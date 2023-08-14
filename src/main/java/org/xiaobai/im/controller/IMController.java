package org.xiaobai.im.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.im.entity.ChatGroupRecordVO;
import org.xiaobai.im.request.HistoryMessagePageRequest;
import org.xiaobai.im.response.ImUserResponse;
import org.xiaobai.im.service.ChatGroupRecordService;
import org.xiaobai.im.service.ImService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName SocketController
 * @Description 聊天室
 * @Author dingfeng.xiao
 * @Date 2023/7/17 13:22
 * @Version 1.0
 */
@Api(value = "聊天室", tags = {"聊天室"})
@CrossOrigin
@RestController
@RequestMapping("/chat/im")
@Validated
public class IMController {

    @Resource
    private ImService imService;

    @Resource
    private ChatGroupRecordService chatGroupRecordService;

    @ApiOperation(value = "所有用户", httpMethod = "POST", response = ImUserResponse.class)
    @PostMapping("/user")
    public Result<List<ImUserResponse>> listResult() {
        return Result.success(imService.userList());
    }

    @ApiOperation(value = "群组消息记录", httpMethod = "POST", response = ChatGroupRecordVO.class)
    @PostMapping("/group/historyMessage")
    public Result<IPage<ChatGroupRecordVO>> historyMessage(@Valid @RequestBody HistoryMessagePageRequest pageRequest) {
        return Result.success(chatGroupRecordService.historyMessage(pageRequest));
    }
}
