package org.xiaobai.ai.request.query;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaobai.common.request.PageQuery;

import javax.validation.constraints.Size;

/**
 * 聊天记录分页查询
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Api(value = "聊天记录分页查询")
public class ChatMessagePageQuery extends PageQuery {

    @ApiParam(value = "聊天室 id")
    private Long chatRoomId;

    @Size(max = 20, message = "ip 字数不能超过 20")
    @ApiParam(value = "ip")
    private String ip;

    @Size(max = 20, message = "问题或回复搜索字数字数不能超过20")
    @ApiParam(value = "问题或回复模糊搜索")
    private String content;
}
