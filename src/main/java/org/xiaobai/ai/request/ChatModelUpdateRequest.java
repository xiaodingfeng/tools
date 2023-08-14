package org.xiaobai.ai.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatModelUpdateRequest {
    @NotNull(message = "ID不能为空")
    private Long id;
    @NotNull(message = "分类ID不能为空")
    private Long cateId;
    @NotBlank(message = "标题不能为空")
    private String title;
    private String ico;
    private String description;
    @NotBlank(message = "systemPrompts不能为空")
    private String systemPrompts;
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
