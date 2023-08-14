package org.xiaobai.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;
import org.xiaobai.common.enums.RedisKeyEnum;
import org.xiaobai.common.response.TokenDTO;
import org.xiaobai.im.handler.ChannelHandlerPool;
import org.xiaobai.im.service.ChatGroupRecordService;

/**
 * @ClassName TokenUtil
 * @Description TokenUtil
 * @Author dingfeng.xiao
 * @Date 2023/7/17 16:09
 * @Version 1.0
 */
public class TokenUtil {
    private final RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);

    private static final TokenUtil tokenUtil = new TokenUtil();

    public static TokenUtil getInstance(){
        return tokenUtil;
    }

    public String getUserId(String token) {
        token = token.replace("bearer ", "");
        String tokenDTO = redisUtil.get(RedisKeyEnum.USER_ACCESS_TOKEN.getSuffix() + token);
        if (!StringUtils.hasText(tokenDTO)) {
            return null;
        }
        TokenDTO object = JSON.parseObject(tokenDTO, TokenDTO.class);
        return object.getUserId();
    }
}
