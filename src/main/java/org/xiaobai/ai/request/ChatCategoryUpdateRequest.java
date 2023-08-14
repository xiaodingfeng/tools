package org.xiaobai.ai.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatCategoryUpdateRequest {
    @NotNull(message = "ID不能为空")
    private Long id;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    private String description;
    private String ico;
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
