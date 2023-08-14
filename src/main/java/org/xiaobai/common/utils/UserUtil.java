package org.xiaobai.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class UserUtil {
    public static Long getUserId() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (Long) requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
    }

    public static String getAccessToken() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (String) requestAttributes.getAttribute("accessToken", RequestAttributes.SCOPE_REQUEST);
    }
}
