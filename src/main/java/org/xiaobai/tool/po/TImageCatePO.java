package org.xiaobai.tool.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TImageCatePO extends BaseIdPO {
    @TableField("cate_name")
    private String cateName;
    @TableField("pid")
    private Long pid;
}
