package org.xiaobai.common.response;

import lombok.Data;

/**
 * @ClassName LoginResponse
 * @Description 登录返回信息
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:43
 * @Version 1.0
 */
@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireTime;
    private Long refreshTokenExpireTime;
}
