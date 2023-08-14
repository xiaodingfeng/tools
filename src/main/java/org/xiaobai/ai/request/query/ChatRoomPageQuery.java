package org.xiaobai.ai.request.query;


import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaobai.common.request.PageQuery;

/**
 * 聊天记录分页查询
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Api(value = "聊天室分页查询")
public class ChatRoomPageQuery extends PageQuery {
    private String keywords;
}
