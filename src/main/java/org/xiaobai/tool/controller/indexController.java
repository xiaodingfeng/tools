package org.xiaobai.tool.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class indexController {

    @Value("${spring.url}")
    private String fromUrl;

    //只要能进这个页面，代表服务运行正常
    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect(fromUrl);
    }
}
