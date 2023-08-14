package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.ChatCategoryVO;
import org.xiaobai.ai.request.query.ChatCategoryPageQuery;

/**
 * 分类
 */
public interface ChatCategoryService extends IService<ChatCategoryVO> {
    /**
     * 模型分类分页查询
     *
     * @param pageQuery 查询条件
     * @return 模型分类分页列表
     */
    IPage<ChatCategoryVO> pageCategory(ChatCategoryPageQuery pageQuery);
}
