package org.xiaobai.core.interceptor;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xiaobai.common.enums.RedisKeyEnum;
import org.xiaobai.common.response.TokenDTO;
import org.xiaobai.core.enums.ErrorCodeEnum;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AuthInterceptor
 * @Description TODO
 * @Author dingfeng.xiao
 * @Date 2023/6/30 15:49
 * @Version 1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active}")
    private String active;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String userId = processAuthHeader(authHeader, request);
            if (!StringUtils.hasText(userId)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new TipException(ErrorCodeEnum.ACCESS_TOKEN_AUTH_FAIL);
            }
            request.setAttribute("userId", Long.valueOf(userId));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new TipException(ErrorCodeEnum.USER_NO_AUTHORIZATION);
        }
        return true;
    }

    private String processAuthHeader(String authHeader, HttpServletRequest request) {
        String token = authHeader.replace("bearer ", "");
        request.setAttribute("accessToken", token);
        String tokenDTO = redisUtil.get(RedisKeyEnum.USER_ACCESS_TOKEN.getSuffix() + token);
        if (!StringUtils.hasText(tokenDTO)) {
            return null;
        }
        TokenDTO object = JSON.parseObject(tokenDTO, TokenDTO.class);
        return object.getUserId();
    }
}
