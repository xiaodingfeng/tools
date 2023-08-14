package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.ChatModelInitWordsVO;
import org.xiaobai.ai.request.query.ChatModelInitWordsPageQuery;

import java.util.List;

/**
 * 模型初始化提示语句
 */
public interface ChatModelInitWordsService extends IService<ChatModelInitWordsVO> {
    /**
     * modelId获取初始化提示prompts
     *
     * @param modeId
     * @return
     */
    List<ChatModelInitWordsVO> listByModel(Long modeId);

    /**
     * 初始化提示prompts分页列表
     *
     * @param pageQuery 查询条件
     * @return 初始化提示prompts分页列表
     */
    IPage<ChatModelInitWordsVO> pageInitWords(ChatModelInitWordsPageQuery pageQuery);
}
