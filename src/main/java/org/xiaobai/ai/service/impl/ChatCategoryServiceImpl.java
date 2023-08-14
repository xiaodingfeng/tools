package org.xiaobai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.entity.ChatCategoryVO;
import org.xiaobai.ai.mapper.ChatCategoryMapper;
import org.xiaobai.ai.request.query.ChatCategoryPageQuery;
import org.xiaobai.ai.service.ChatCategoryService;
import org.xiaobai.core.utils.PageUtil;

import java.util.List;

@Service
public class ChatCategoryServiceImpl extends ServiceImpl<ChatCategoryMapper, ChatCategoryVO> implements ChatCategoryService {
    @Override
    public List<ChatCategoryVO> list() {
        return list(new LambdaQueryWrapper<ChatCategoryVO>()
                .orderByAsc(ChatCategoryVO::getSort));
    }

    @Override
    public IPage<ChatCategoryVO> pageCategory(ChatCategoryPageQuery pageQuery) {
        IPage<ChatCategoryVO> categoryPage = page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()),
                new LambdaQueryWrapper<ChatCategoryVO>()
                        .and(StrUtil.isNotEmpty(pageQuery.getKeywords()), v -> {
                            v.like(ChatCategoryVO::getDescription, pageQuery.getKeywords())
                                    .or()
                                    .like(ChatCategoryVO::getName, pageQuery.getKeywords());
                        }));

        return PageUtil.toPage(categoryPage, categoryPage.getRecords());
    }
}
