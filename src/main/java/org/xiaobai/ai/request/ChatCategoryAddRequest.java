package org.xiaobai.ai.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类新增请求
 */
@Data
public class ChatCategoryAddRequest {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    private String description;
    private String ico;
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
