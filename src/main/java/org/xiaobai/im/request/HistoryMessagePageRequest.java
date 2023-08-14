package org.xiaobai.im.request;

import lombok.Data;
import org.xiaobai.common.request.PageQuery;

/**
 * im 历史消息分页
 */
@Data
public class HistoryMessagePageRequest extends PageQuery {
    private String keywords;
    private Long groupId;
}
