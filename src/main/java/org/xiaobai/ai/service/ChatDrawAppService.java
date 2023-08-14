package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.entity.ChatDrawAppVO;
import org.xiaobai.ai.request.ChatDrawAppAddTaskRequest;
import org.xiaobai.ai.request.ChatDrawAppMyPageRequest;
import org.xiaobai.ai.response.ChatDrawAppMyPageResponse;

import java.io.IOException;

/**
 * @ClassName ChatDrawAppService
 * @Description 用户画图信息
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:09
 * @Version 1.0
 */
public interface ChatDrawAppService extends IService<ChatDrawAppVO> {
    /**
     * 用户画图信息分页
     *
     * @param userId
     * @param drawAppMyPageRequest
     * @return List<ChatDrawAppMyPageResponse>
     */
    Page<ChatDrawAppMyPageResponse> myListPage(Long userId, ChatDrawAppMyPageRequest drawAppMyPageRequest);

    /**
     * 新增任务
     *
     * @param userId
     * @param drawAppAddTaskRequest
     * @return Long 图片信息ID，可根据id获取进度
     */
    ResponseBodyEmitter addTask(Long userId, ChatDrawAppAddTaskRequest drawAppAddTaskRequest) throws IOException;
}
