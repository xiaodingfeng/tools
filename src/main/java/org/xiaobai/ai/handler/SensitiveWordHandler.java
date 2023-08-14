package org.xiaobai.ai.handler;


import cn.hutool.dfa.WordTree;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.xiaobai.ai.api.enums.EnableDisableStatusEnum;
import org.xiaobai.ai.entity.SensitiveWordVO;
import org.xiaobai.ai.mapper.SensitiveWordMapper;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 敏感词处理
 */
@Slf4j
public class SensitiveWordHandler {

    private static final String CACHE_KEY = "wordTree";

    /**
     * 敏感词缓存
     */
    private static final LoadingCache<String, WordTree> CACHE = CacheBuilder.newBuilder()
            // 设置并发级别为 CPU 核心数
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            // 容量为 1
            .initialCapacity(1)
            // 过期时间为 12 小时
            .expireAfterWrite(12, TimeUnit.HOURS)
            .build(new CacheLoader<>() {

                @Override
                public @NotNull WordTree load(@NotNull String s) {
                    log.warn("开始构建敏感词树");
                    WordTree wordTree = new WordTree();
                    SensitiveWordMapper sensitiveWordMapper = SpringUtil.getBean(SensitiveWordMapper.class);
                    List<SensitiveWordVO> sensitiveWords = sensitiveWordMapper.selectList(new LambdaQueryWrapper<SensitiveWordVO>()
                            .select(SensitiveWordVO::getWord)
                            .eq(SensitiveWordVO::getStatus, EnableDisableStatusEnum.ENABLE));
                    log.warn("查询数据库，敏感词数量为：{} 个", sensitiveWords.size());
                    // 生成关键词树
                    wordTree.addWords(sensitiveWords.stream().map(SensitiveWordVO::getWord).collect(Collectors.toSet()));
                    return wordTree;
                }
            });

    /**
     * 检查敏感词
     *
     * @return 敏感词列表
     */
    public static List<String> checkWord(String content) {
        WordTree wordTree = null;
        try {
            wordTree = CACHE.get(CACHE_KEY);
        } catch (Exception e) {
            log.error("获取敏感词树失败", e);
        }
        if (Objects.isNull(wordTree)) {
            return Collections.emptyList();
        }
        return wordTree.matchAll(content, -1, false, false);
    }
}

