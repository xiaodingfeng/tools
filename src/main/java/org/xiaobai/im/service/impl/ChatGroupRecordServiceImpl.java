package org.xiaobai.im.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.xiaobai.common.entity.BaseEntity;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.utils.IDistributedLockUtil;
import org.xiaobai.core.utils.IpUtil;
import org.xiaobai.core.utils.PageUtil;
import org.xiaobai.im.entity.ChatGroupRecordVO;
import org.xiaobai.im.mapper.ChatGroupRecordMapper;
import org.xiaobai.im.request.HistoryMessagePageRequest;
import org.xiaobai.im.service.ChatGroupRecordService;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName ChatGroupRecordServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/17 14:48
 * @Version 1.0
 */
@Service
public class ChatGroupRecordServiceImpl extends ServiceImpl<ChatGroupRecordMapper, ChatGroupRecordVO> implements ChatGroupRecordService {
    /**
     * 默认群组
     */
    private final static Long DEFAULT_GROUP_ID = 0L;

    @Resource
    private IDistributedLockUtil iDistributedLockUtil;

    @Resource
    private SysUserService sysUserService;

    @Override
    public Long addMessage(ChatGroupRecordVO chatGroupRecord) {
        if (Objects.isNull(chatGroupRecord)) {
            return null;
        }
        if (Objects.isNull(chatGroupRecord.getGroupId())) {
            chatGroupRecord.setGroupId(DEFAULT_GROUP_ID);
        }
        chatGroupRecord.setIpSource(IpUtil.searchIp(chatGroupRecord.getIpAddress()));
        chatGroupRecord.setType(0);
        RLock lock = iDistributedLockUtil.lock("chat_group_record:" + chatGroupRecord.getGroupId());
        try {
            save(chatGroupRecord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return chatGroupRecord.getId();
    }

    @Override
    public IPage<ChatGroupRecordVO> historyMessage(HistoryMessagePageRequest pageRequest) {
        if (Objects.isNull(pageRequest.getGroupId())) {
            pageRequest.setGroupId(DEFAULT_GROUP_ID);
        }
        final Long userId = UserUtil.getUserId();
        Page<ChatGroupRecordVO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        page.setOptimizeCountSql(false);
        IPage<ChatGroupRecordVO> categoryPage = page(page,
                new LambdaQueryWrapper<ChatGroupRecordVO>()
                        .eq(ChatGroupRecordVO::getGroupId, pageRequest.getGroupId())
                        .and(StrUtil.isNotEmpty(pageRequest.getKeywords()), v -> {
                            v.like(ChatGroupRecordVO::getContent, pageRequest.getKeywords());
                        })
                        .orderByDesc(BaseEntity::getCreateTime));
        categoryPage.setRecords(categoryPage.getRecords().stream().peek(v -> {
            v.setUser(sysUserService.getById(v.getUserId()));
            if (v.getUserId().equals(userId)) {
                v.setMessageType("QUESTION");
            } else {
                v.setMessageType("ANSWER");
            }
        }).collect(Collectors.toList()));

        return PageUtil.toPage(categoryPage, categoryPage.getRecords());
    }
}
