package org.xiaobai.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaobai.ai.entity.ChatConfigVO;
import org.xiaobai.ai.entity.ChatRoomVO;

/**
 * chat 配置
 */
@Mapper
public interface ChatConfigMapper extends BaseMapper<ChatConfigVO> {
}
