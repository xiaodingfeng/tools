package org.xiaobai.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName BaseId
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:07
 * @Version 1.0
 */
@Data
public class BaseId {
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
}
