package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.request.query.ChatModelPageQuery;

import java.util.List;
import java.util.Map;

/**
 * 模型列表
 */
public interface ChatModelService extends IService<ChatModelVO> {

    /**
     * 分类分组获取模型列表
     * @return
     */
    Map<String, List<ChatModelVO>> listGroupByCate();

    List<ChatModelVO> listByCate(Long cateId);

    /**
     * 模型分页查询
     *
     * @param pageQuery 查询条件
     * @return 模型分页查询列表
     */
    IPage<ChatModelVO> pageModel(ChatModelPageQuery pageQuery);


}
