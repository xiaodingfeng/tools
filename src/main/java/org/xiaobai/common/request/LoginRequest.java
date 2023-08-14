package org.xiaobai.common.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName LoginRequest
 * @Description 登录请求信息
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:46
 * @Version 1.0
 */
@Data
public class LoginRequest {
    @NotBlank(message = "邮箱不能为空")
    private String email;
    //@NotBlank(message = "验证码不能为空")
    private String captcha;
    //@NotBlank(message = "密码不能为空")
    private String password;
}
