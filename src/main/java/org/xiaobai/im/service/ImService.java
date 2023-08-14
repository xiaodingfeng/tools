package org.xiaobai.im.service;

import org.xiaobai.im.response.ImUserResponse;

import java.util.List;

/**
 * @ClassName ImService
 * @Description im
 * @Author dingfeng.xiao
 * @Date 2023/7/17 13:29
 * @Version 1.0
 */
public interface ImService {
    /**
     * 获取所有用户
     *
     * @return
     */
    List<ImUserResponse> userList();
}
