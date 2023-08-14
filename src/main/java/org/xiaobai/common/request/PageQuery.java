package org.xiaobai.common.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 分页参数
 */
@Data
@Valid
public class PageQuery {

    @NotNull(message = "第几页不能为空")
    private Integer pageSize;

    @NotNull(message = "每页条数不能为空")
    private Integer pageNum;
}

