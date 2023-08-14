package org.xiaobai.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName ThreadLocalUtil
 * @Description ThreadLocalUtil
 * @Author dingfeng.xiao
 * @Date 2023/8/9 8:53
 * @Version 1.0
 */
@Component
public class EnvironmentUtil {

    @Resource
    private Environment env;

    public String active() {
        return env.getProperty("spring.profiles.active");
    }
}
