package org.xiaobai.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaobai.ai.entity.SensitiveWordVO;

/**
 * 敏感词数据库访问层
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWordVO> {

}
