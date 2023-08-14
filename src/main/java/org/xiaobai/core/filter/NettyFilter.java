//package org.xiaobai.core.filter;
//
//import org.springframework.stereotype.Component;
//import org.xiaobai.core.config.NettyConfig;
//
//import javax.annotation.Resource;
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@WebFilter(filterName = "NettyFilter", urlPatterns = "/ws/**")
//public class NettyFilter implements Filter {
//
//    @Resource
//    NettyConfig nettyConfig;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        response.setHeader(nettyConfig.getAuthHeader(), request.getHeader(nettyConfig.getAuthHeader()));
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
