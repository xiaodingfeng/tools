package org.xiaobai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.entity.ChatModelInitWordsVO;
import org.xiaobai.ai.mapper.ChatModelInitWordsMapper;
import org.xiaobai.ai.request.query.ChatModelInitWordsPageQuery;
import org.xiaobai.ai.service.ChatModelInitWordsService;
import org.xiaobai.core.utils.PageUtil;

import java.util.List;
import java.util.Objects;

@Service
public class ChatModelInitWordsServiceImpl extends ServiceImpl<ChatModelInitWordsMapper, ChatModelInitWordsVO> implements ChatModelInitWordsService {
    @Override
    public List<ChatModelInitWordsVO> listByModel(Long modeId) {
        return list(new LambdaQueryWrapper<ChatModelInitWordsVO>()
                .eq(ChatModelInitWordsVO::getModelId, modeId)
                .orderByDesc(ChatModelInitWordsVO::getId));
    }

    @Override
    public IPage<ChatModelInitWordsVO> pageInitWords(ChatModelInitWordsPageQuery pageQuery) {
        IPage<ChatModelInitWordsVO> chatModelInitWordsPage = page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()),
                new LambdaQueryWrapper<ChatModelInitWordsVO>()
                        .eq(Objects.nonNull(pageQuery.getModelId()), ChatModelInitWordsVO::getModelId, pageQuery.getModelId())
                .and(StrUtil.isNotEmpty(pageQuery.getKeywords()), v -> {
                    v.like(ChatModelInitWordsVO::getPrompts, pageQuery.getKeywords())
                            .or()
                            .eq(ChatModelInitWordsVO::getModelId, pageQuery.getKeywords());
                }));

        return PageUtil.toPage(chatModelInitWordsPage, chatModelInitWordsPage.getRecords());
    }
}
