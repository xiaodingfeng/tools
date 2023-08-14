package org.xiaobai.ai.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.entity.ChatRoomVO;
import org.xiaobai.ai.request.ChatUserChatAddRequest;
import org.xiaobai.ai.request.query.ChatRoomSearchRequest;
import org.xiaobai.ai.response.RoomInitResponse;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.ai.service.ChatRoomService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName ChatModelInitWordsController
 * @Description 模型初始化提示prompts
 * @Author dingfeng.xiao
 * @Date 2023/7/1 15:45
 * @Version 1.0
 */
@Api(value = "用户模型会话", tags = {"用户模型会话"})
@CrossOrigin
@RestController
@RequestMapping("/chat/user/chat")
@Validated
public class ChatUserChatController {
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private ChatRoomService chatRoomService;

    /**
     * 新增用户模型会话
     */
    @ApiOperation(value = "新增用户模型会话")
    @PostMapping("/addChat")
    public Result<Long> addChat(@Valid @RequestBody ChatUserChatAddRequest addRequest) {
        return Result.success(chatRoomService.createChatRoom(addRequest));
    }

    /**
     * 用户模型会话列表
     */
    @ApiOperation(value = "用户模型会话列表")
    @PostMapping("/my")
    public Result<List<ChatRoomVO>> list(@Valid @RequestBody ChatRoomSearchRequest searchRequest) {
        return Result.success(chatRoomService.userList(searchRequest));
    }

    /**
     * 用户模型会话删除
     */
    @ApiOperation(value = "用户模型会话删除")
    @PostMapping("/my/remove")
    public Result<Boolean> removeRoom(@NotNull Long roomId) {
        return Result.success(chatRoomService.removeRoom(roomId));
    }

    /**
     * 用户模型会话明细列表
     */
    @ApiOperation(value = "用户模型会话明细列表")
    @PostMapping("/my/message")
    public Result<List<ChatMessageVO>> listChat(@NotNull Long roomId) {
        return Result.success(chatRoomService.listChat(roomId));
    }

    /**
     * 用户模型会话详情
     */
    @ApiOperation(value = "用户模型会话详情")
    @PostMapping("/get")
    public Result<ChatRoomVO> get(@NotNull Long id) {
        return Result.success(chatRoomService.getById(id));
    }

    @ApiOperation(value = "发送消息")
    @PostMapping("/send")
    public ResponseBodyEmitter sendMessage(@RequestBody @Valid ChatProcessRequest chatProcessRequest, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        chatProcessRequest.setUserId(UserUtil.getUserId());
        return chatMessageService.sendMessage(chatProcessRequest);
    }

    /**
     * 用户会话初始化接口
     */
    @ApiOperation(value = "用户会话初始化接口")
    @PostMapping("/initData")
    public Result<RoomInitResponse> initData(@NotNull Long id) {
        return Result.success(chatRoomService.initData(id));
    }

    /**
     * 用户会话初始化接口
     */
    @ApiOperation(value = "用户导出历史消息记录")
    @PostMapping("/exportHistoryMessage")
    public void exportHistoryMessage(HttpServletResponse response,
                                     @NotNull(message = "房间ID不能为空") Long roomId,
                                                @NotBlank(message = "导出类型不能为空") @RequestParam(defaultValue = "MARK_DOWN") String exportType) {
        chatRoomService.exportHistoryMessage(response, roomId, exportType);
    }
}
