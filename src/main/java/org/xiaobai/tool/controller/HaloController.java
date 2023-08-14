package org.xiaobai.tool.controller;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.core.utils.JsoupUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/tool/halo")
public class HaloController {
    @RequestMapping("/randomBabyImage")
    public void randBabyImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Enumeration<String> enumeration = request.getHeaderNames();
            Map<String, String> headers = new HashMap<>();
            Iterator<String> iterator = enumeration.asIterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                String header = request.getHeader(next);
                headers.put(next, header);
            }
            Cookie[] cookies = request.getCookies();
            Map<String, String> cookie = new HashMap<>();
            for (Cookie cookie1 : cookies) {
                cookie.put(cookie1.getName(), cookie1.getValue());
            }
            Connection connect = JsoupUtil.connect("http://fengzhengx.cn/api/content/photos?_r=1669942844843&page=0&size=200&sort=createTime%2Cdesc&team=%E5%AE%9D%E8%B4%9D");
            connect.cookies(cookie);
            connect.headers(headers);
            String body = connect.method(Connection.Method.GET).execute().body();
            System.out.println(body);
            JSONObject object = JSONObject.parseObject(body);
            if ((Integer.parseInt(object.get("status").toString()) == 200)) {
                for (Object o : object.getJSONObject("status").getJSONObject("data").getJSONArray("content")) {
                    Object thumbnail = JSONObject.parseObject(o.toString()).get("thumbnail");
                    System.out.println(thumbnail);
                    response.sendRedirect(thumbnail.toString());
                    return;
                }
            }
            response.sendRedirect("https://bing.ioliu.cn/v1/rand?w=400&h=300");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("https://bing.ioliu.cn/v1/rand?w=400&h=300");
        }
    }
}
