package org.xiaobai.ai.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatModelInitWordsUpdateRequest {
    @NotNull(message = "ID不能为空")
    private Long id;
    @NotNull(message = "模型ID不能为空")
    private Long modelId;
    @NotBlank(message = "prompts不能为空")
    private String prompts;
}
