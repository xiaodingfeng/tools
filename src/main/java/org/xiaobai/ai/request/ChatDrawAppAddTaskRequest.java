package org.xiaobai.ai.request;

import lombok.Data;

/**
 * @ClassName ChatDrawAppAddTaskRequest
 * @Description 新增画图任务请求
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:52
 * @Version 1.0
 */
@Data
public class ChatDrawAppAddTaskRequest {
    private String mode;
    private String config;
    private String prompt;
    private Integer status;
}
