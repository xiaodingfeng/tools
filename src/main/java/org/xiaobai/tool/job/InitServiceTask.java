package org.xiaobai.tool.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.xiaobai.ai.vip.queue.OrderTimeoutQueue;
import org.xiaobai.tool.service.ApiService;
import org.xiaobai.tool.service.TImagesService;
import org.xiaobai.tool.wechat.WeChatApplicationService;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Configuration
@EnableScheduling
public class InitServiceTask {

    @Autowired
    private ApiService apiService;

    @Autowired
    private TImagesService tImagesService;

    @Autowired
    private WeChatApplicationService bspAppService;

    @Resource
    private OrderTimeoutQueue orderTimeoutQueue;

    @Scheduled(cron = "0 20 5 * * ?")
    private void configureTasks() throws IOException {
        apiService.bingImage(null, null);
        apiService.meiriyiwen(null);
        apiService.historyToday(null);
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    private void processTimeoutOrders() throws InterruptedException {
        orderTimeoutQueue.processTimeoutOrders();
    }
}
