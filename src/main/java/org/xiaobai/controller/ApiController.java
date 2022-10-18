package org.xiaobai.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.response.*;
import org.xiaobai.service.ApiService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xdf
 */
@RestController
@RequestMapping("/")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @ApiOperation(value = "每日一句", httpMethod = "get, post", response = String.class)
    @RequestMapping("/oneWord")
    public Result<String> oneWord() throws IOException {
        return Result.success(apiService.oneWord());
    }

    @ApiOperation(value = "qq信息", httpMethod = "get, post", response = QQInfoResponse.class)
    @RequestMapping("/qInfo")
    public Result<QQInfoResponse> oneWord(String qq) throws IOException {
        return Result.success(apiService.qInfo(qq));
    }

    @ApiOperation(value = "二维码", httpMethod = "get, post", response = Object.class)
    @RequestMapping("/qrCode")
    public Result<Object> qrCode(String msg,
                                 Integer width,
                                 Integer height,
                                 HttpServletResponse response) throws IOException {
        apiService.qrCode(msg,width, height, response);
        return Result.success();
    }

    @ApiOperation(value = "邮件", httpMethod = "get, post", response = Object.class)
    @RequestMapping("/sendMail")
    public Result<Object> sendMail(String to,
                                 String title,
                                 String content) throws Exception {
        apiService.sendMail(to,title, content);
        return Result.success();
    }


    @ApiOperation(value = "bing", httpMethod = "get, post", response = BingImage.class)
    @RequestMapping("/bingImage")
    public Result<BingImage> bingImage(Integer isSendRedirect,
                                       HttpServletResponse response) throws Exception {
        return Result.success(apiService.bingImage(isSendRedirect,response));
    }

    @ApiOperation(value = "历史上的今天", httpMethod = "get, post", response = List.class)
    @RequestMapping("/historyToday")
    public Result<List<HistoryToday>> historyToday(String date) throws Exception {
        return Result.success(apiService.historyToday(date));
    }

    @ApiOperation(value = "看图猜成语", httpMethod = "get, post", response = Ktccy.class)
    @RequestMapping("/ktccy")
    public Result<Ktccy> ktccy() throws Exception {
        return Result.success(apiService.ktccy());
    }

    @ApiOperation(value = "每日一文", httpMethod = "get, post", response = Ktccy.class)
    @RequestMapping("/meiriyiwen")
    public Result<MeiRiYiWen> meiriyiwen(String date) throws Exception {
        return Result.success(apiService.meiriyiwen(date));
    }
}
