package org.xiaobai.common.enums;

/**
 * @ClassName RedisKeyEnum
 * @Description redis key枚举类
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:50
 * @Version 1.0
 */
public enum RedisKeyEnum {
    USER_LOGIN_CAPTCHA("user_login_captcha:", "用户邮箱验证码"),
    USER_ACCESS_TOKEN("user_access_token:", "用户登录accesstoken"),
    USER_REFRESH_TOKEN("user_refresh_token:", "用户登录refreshToken");
    private final String suffix;
    private final String remark;

    RedisKeyEnum(String suffix, String remark) {
        this.suffix = suffix;
        this.remark = remark;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getRemark() {
        return remark;
    }
}
