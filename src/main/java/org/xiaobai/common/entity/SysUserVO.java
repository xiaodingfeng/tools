package org.xiaobai.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:21
 * @Version 1.0
 */
@Data
@TableName("`t_sys_user`")
public class SysUserVO extends BaseEntity {
    @TableField("user_name")
    private String userName;
    @TableField("nick_name")
    private String nickName;
    private String email;
    @JsonIgnore
    private String password;
    private String avatar;
}
