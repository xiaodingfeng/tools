package org.xiaobai.ai.request.query;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaobai.ai.api.enums.EnableDisableStatusEnum;
import org.xiaobai.common.request.PageQuery;

import javax.validation.constraints.Size;

/**
 * 敏感词分页查询
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Api(value = "敏感词分页查询")
public class SensitiveWordPageQuery extends PageQuery {

    @Size(max = 20, message = "敏感词内容不超过 20 个字")
    @ApiParam(value = "敏感词内容")
    private String word;

    @ApiParam(value = "状态 1 启用 2 停用")
    private EnableDisableStatusEnum status;
}
