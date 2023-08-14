package org.xiaobai.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaobai.common.entity.SysUserVO;

/**
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:20
 * @Version 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserVO> {
}
