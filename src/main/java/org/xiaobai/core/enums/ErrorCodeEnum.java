package org.xiaobai.core.enums;

/**
 * @ClassName ErrorCodeEnum
 * @Description 错误码信息枚举
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:54
 * @Version 1.0
 */
public enum ErrorCodeEnum {
    /**
     * 权限类
     */
    USER_NO_AUTHORIZATION(4000001, "Authorization header 不存在"),
    ACCESS_TOKEN_AUTH_FAIL(4000002, "access_token无效"),
    USER_LOGIN_CAPTCHA_FAIL(4000003, "验证码无效"),
    USER_LOGIN_CAPTCHA_WAIT(4000004, "验证码发送超时，请稍后获取"),
    ADMIN_LOGIN_FAIL(4000005, "admin登录失败"),
    REFRESH_TOKEN_NOT_EXITS(4000006, "refresh_token无效"),
    /**
     * chat类
     */
    CHAT_USER_CHAT_NOT_FOUND(600001, "会话不存在");
    private final Integer code;
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
