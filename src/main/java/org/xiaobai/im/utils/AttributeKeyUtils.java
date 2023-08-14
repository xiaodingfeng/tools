package org.xiaobai.im.utils;

import io.netty.util.AttributeKey;
import org.xiaobai.im.handler.SecurityServerHandler;

public class AttributeKeyUtils {
    /**
     * 为channel添加属性  将userid设置为属性，避免客户端特殊情况退出时获取不到userid
     */
    public static final AttributeKey<String> USER_ID = AttributeKey.valueOf("userid");

    public static final AttributeKey<SecurityServerHandler.SecurityCheckComplete> SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY =
            AttributeKey.valueOf("SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY");

}
