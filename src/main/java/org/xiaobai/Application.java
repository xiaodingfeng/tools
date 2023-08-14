package org.xiaobai;

import io.netty.bootstrap.ServerBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;

@EnableAsync
@SpringBootApplication
public class Application {
    @Resource
    private ServerBootstrap serverBootstrap;
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
