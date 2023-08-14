package org.xiaobai.tool.po;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseIdPO {
    @TableId
    private Long id;
}
