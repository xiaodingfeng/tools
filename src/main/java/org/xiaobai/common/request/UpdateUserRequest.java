package org.xiaobai.common.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @ClassName UpdateUserRequest
 * @Author dingfeng.xiao
 * @Date 2023/7/24 15:45
 * @Version 1.0
 */
@Data
public class UpdateUserRequest {
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "头像不能为空")
    private String avatar;
    @Length(min = 6, max = 16, message = "密码长度需为6-16位")
    private String password;
}
