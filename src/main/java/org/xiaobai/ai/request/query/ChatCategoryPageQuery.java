package org.xiaobai.ai.request.query;

import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaobai.common.request.PageQuery;

/**
 * 敏感词分页查询
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Api(value = "模型分类分页查询")
public class ChatCategoryPageQuery extends PageQuery {
    private String keywords;
}
