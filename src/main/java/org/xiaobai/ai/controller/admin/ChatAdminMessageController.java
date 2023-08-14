package org.xiaobai.ai.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.request.query.ChatMessagePageQuery;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 聊天记录相关接口
 */
@AllArgsConstructor
@Api(value = "管理端-聊天记录相关接口")
@RestController
@RequestMapping("/chat/admin/chat_message")
public class ChatAdminMessageController {

    @Resource
    private final ChatMessageService chatMessageService;

    @ApiOperation(value = "分页列表")
    @PostMapping("/page")
    public Result<IPage<ChatMessageVO>> page(@Valid @RequestBody ChatMessagePageQuery chatMessagePageQuery) {
        return Result.success(chatMessageService.pageChatMessage(chatMessagePageQuery));
    }
}
