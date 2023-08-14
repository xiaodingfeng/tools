package org.xiaobai.ai.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.SensitiveWordVO;
import org.xiaobai.ai.request.query.SensitiveWordPageQuery;

/**
 * 敏感词业务接口
 */
public interface SensitiveWordService extends IService<SensitiveWordVO> {

    /**
     * 敏感词分页查询
     *
     * @param sensitiveWordPageQuery 查询条件
     * @return 敏感词分页列表
     */
    IPage<SensitiveWordVO> pageSensitiveWord(SensitiveWordPageQuery sensitiveWordPageQuery);
}
