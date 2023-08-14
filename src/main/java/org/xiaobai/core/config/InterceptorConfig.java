package org.xiaobai.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiaobai.core.interceptor.AuthInterceptor;

/**
 * @ClassName InterceptorConfig
 * @Description 拦截器配置
 * @Author dingfeng.xiao
 * @Date 2023/6/30 15:57
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Autowired
    public InterceptorConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/chat/**")
                .addPathPatterns("/sys/user/getInfo")
                .addPathPatterns("/sys/user/updateUser")
                .excludePathPatterns("/chat/admin/login"); // 拦截 /chat/ 路径下的所有请求
    }
}
