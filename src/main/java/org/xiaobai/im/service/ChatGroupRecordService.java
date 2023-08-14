package org.xiaobai.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.im.entity.ChatGroupRecordVO;
import org.xiaobai.im.request.HistoryMessagePageRequest;

/**
 * 群组消息
 */
public interface ChatGroupRecordService extends IService<ChatGroupRecordVO> {
    /**
     * 新增消息记录
     *
     * @param chatGroupRecord
     * @return
     */
    Long addMessage(ChatGroupRecordVO chatGroupRecord);

    /**
     * 获取群组历史消息记录
     *
     * @param pageRequest
     * @return
     */
    IPage<ChatGroupRecordVO> historyMessage(HistoryMessagePageRequest pageRequest);
}
