package org.xiaobai.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaobai.ai.entity.ChatRoomVO;

/**
 * 聊天室数据访问层
 */
@Mapper
public interface ChatRoomMapper extends BaseMapper<ChatRoomVO> {
}
