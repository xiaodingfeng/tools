package org.xiaobai.ai.request.query;

import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaobai.common.request.PageQuery;

@Data
@EqualsAndHashCode(callSuper = false)
@Api(value = "初始化提示prompts分页查询")
public class ChatModelInitWordsPageQuery extends PageQuery {
    private Long modelId;
    private String keywords;
}
