package org.xiaobai.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.xiaobai.service.ApiService;

import java.io.IOException;

@Slf4j
@Configuration
@EnableScheduling
public class InitServiceTask {

    @Autowired
    private ApiService apiService;

    @Scheduled(cron = "0 20 5 * * ?")
    private void configureTasks() throws IOException {
        apiService.bingImage(null, null);
        apiService.meiriyiwen(null);
        apiService.historyToday(null);
    }
}
