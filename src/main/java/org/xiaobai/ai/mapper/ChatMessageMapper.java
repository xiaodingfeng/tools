package org.xiaobai.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaobai.ai.entity.ChatMessageVO;

/**
 * 聊天记录数据访问层
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessageVO> {
}
