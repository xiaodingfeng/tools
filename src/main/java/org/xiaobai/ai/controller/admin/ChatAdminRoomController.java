package org.xiaobai.ai.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatRoomVO;
import org.xiaobai.ai.request.query.ChatRoomPageQuery;
import org.xiaobai.ai.service.ChatRoomService;
import org.xiaobai.tool.response.Result;

import javax.validation.Valid;

/**
 * 聊天室相关接口
 */
@AllArgsConstructor
@Api(value = "管理端-聊天室相关接口")
@RequestMapping("/chat/admin/chat_room")
@RestController
public class ChatAdminRoomController {

    private final ChatRoomService chatRoomService;

    @ApiOperation(value = "聊天室分页列表")
    @PostMapping("/page")
    public Result<IPage<ChatRoomVO>> page(@Valid @RequestBody ChatRoomPageQuery chatRoomPageQuery) {
        return Result.success(chatRoomService.pageChatRoom(chatRoomPageQuery));
    }
}
