package org.xiaobai.ai.request.query;

import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaobai.common.request.PageQuery;

@Data
@EqualsAndHashCode(callSuper = false)
@Api(value = "模型分页查询")
public class ChatModelPageQuery extends PageQuery {
    private String keywords;
}
