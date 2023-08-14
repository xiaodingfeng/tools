package org.xiaobai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.mapper.ChatModelMapper;
import org.xiaobai.ai.request.query.ChatModelPageQuery;
import org.xiaobai.ai.service.ChatCategoryService;
import org.xiaobai.ai.service.ChatModelService;
import org.xiaobai.core.utils.PageUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatModelServiceImpl extends ServiceImpl<ChatModelMapper, ChatModelVO> implements ChatModelService {

    @Resource
    private ChatCategoryService  chatCategoryService;

    @Override
    public Map<String, List<ChatModelVO>> listGroupByCate() {
        return list().stream()
                .peek(v -> v.setCateName(chatCategoryService.getById(v.getCateId()).getName())).collect(
                Collectors.groupingBy(
                        ChatModelVO::getCateName
                ));
    }

    @Override
    public List<ChatModelVO> listByCate(Long cateId) {
        return list(new LambdaQueryWrapper<ChatModelVO>()
                .eq(ChatModelVO::getCateId, cateId));
    }

    @Override
    public IPage<ChatModelVO> pageModel(ChatModelPageQuery pageQuery) {
        IPage<ChatModelVO> categoryPage = page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()),
                new LambdaQueryWrapper<ChatModelVO>()
                        .and(StrUtil.isNotEmpty(pageQuery.getKeywords()), v -> {
                            v.like(ChatModelVO::getDescription, pageQuery.getKeywords())
                                    .or()
                                    .like(ChatModelVO::getTitle, pageQuery.getKeywords())
                                    .or()
                                    .like(ChatModelVO::getSystemPrompts, pageQuery.getKeywords())
                                    .or()
                                    .eq(ChatModelVO::getCateId, pageQuery.getKeywords())
                                    .or()
                                    .eq(ChatModelVO::getId, pageQuery.getKeywords());
                        }));

        return PageUtil.toPage(categoryPage, categoryPage.getRecords());
    }
}
