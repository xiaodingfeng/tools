package org.xiaobai.ai.service;


import org.xiaobai.ai.entity.RateLimitVO;

import java.util.List;

/**
 * 限流记录业务接口
 */
public interface RateLimitService {

    /**
     * 查询限流列表
     *
     * @return 限流列表
     */
    List<RateLimitVO> listRateLimit();
}
