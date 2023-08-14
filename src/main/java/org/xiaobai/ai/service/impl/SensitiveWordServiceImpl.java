package org.xiaobai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.entity.SensitiveWordVO;
import org.xiaobai.ai.mapper.SensitiveWordMapper;
import org.xiaobai.ai.request.query.SensitiveWordPageQuery;
import org.xiaobai.ai.service.SensitiveWordService;
import org.xiaobai.core.utils.PageUtil;

import java.util.Objects;

/**
 * 敏感词业务实现类
 */
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWordVO> implements SensitiveWordService {

    @Override
    public IPage<SensitiveWordVO> pageSensitiveWord(SensitiveWordPageQuery sensitiveWordPageQuery) {
        IPage<SensitiveWordVO> sensitiveWordPage = page(new Page<>(sensitiveWordPageQuery.getPageNum(), sensitiveWordPageQuery.getPageSize()),
                new LambdaQueryWrapper<SensitiveWordVO>()
                        .eq(Objects.nonNull(sensitiveWordPageQuery.getStatus()), SensitiveWordVO::getStatus, sensitiveWordPageQuery.getStatus())
                        .like(StrUtil.isNotBlank(sensitiveWordPageQuery.getWord()), SensitiveWordVO::getWord, sensitiveWordPageQuery.getWord())
                        .orderByDesc(SensitiveWordVO::getCreateTime));

        return PageUtil.toPage(sensitiveWordPage, sensitiveWordPage.getRecords());
    }
}
