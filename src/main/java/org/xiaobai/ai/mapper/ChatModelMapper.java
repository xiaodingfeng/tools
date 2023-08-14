package org.xiaobai.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaobai.ai.entity.ChatModelVO;

@Mapper
public interface ChatModelMapper extends BaseMapper<ChatModelVO> {
}
