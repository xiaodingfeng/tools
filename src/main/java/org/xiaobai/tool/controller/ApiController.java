package org.xiaobai.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.tool.response.*;
import org.xiaobai.tool.service.ApiService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xdf
 */
@Api(value = "工具接口", tags = {"工具接口"})
@CrossOrigin
@RestController
@RequestMapping("/tool")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @ApiOperation(value = "每日一句", httpMethod = "GET", response = String.class)
    @GetMapping("/oneWord")
    public Result<String> oneWord() throws IOException {
        return Result.success(apiService.oneWord());
    }

    @ApiOperation(value = "qq信息", httpMethod = "GET", response = QQInfoResponse.class)
    @GetMapping("/qInfo")
    public Result<QQInfoResponse> oneWord(String qq) throws IOException {
        return Result.success(apiService.qInfo(qq));
    }

    @ApiOperation(value = "二维码", httpMethod = "GET", response = Object.class)
    @GetMapping("/qrCode")
    public Result<Object> qrCode(String msg,
                                 Integer width,
                                 Integer height,
                                 HttpServletResponse response) throws IOException {
        apiService.qrCode(msg, width, height, response);
        return Result.success();
    }

    @ApiOperation(value = "邮件发送", httpMethod = "GET", response = Object.class)
    @GetMapping("/sendMail")
    public Result<Object> sendMail(String to,
                                   String title,
                                   String content) throws Exception {
        apiService.sendMail(to, title, content);
        return Result.success();
    }

    @ApiOperation(value = "bing每日一图", httpMethod = "GET", response = BingImage.class)
    @GetMapping("/bingImage")
    public Result<BingImage> bingImage(Integer isSendRedirect,
                                       HttpServletResponse response) throws Exception {
        return Result.success(apiService.bingImage(isSendRedirect, response));
    }

    @ApiOperation(value = "历史上的今天", httpMethod = "GET", response = List.class)
    @GetMapping("/historyToday")
    public Result<List<HistoryToday>> historyToday(String date) throws Exception {
        return Result.success(apiService.historyToday(date));
    }

    @ApiOperation(value = "看图猜成语", httpMethod = "GET", response = Ktccy.class)
    @GetMapping("/ktccy")
    public Result<Ktccy> ktccy() throws Exception {
        return Result.success(apiService.ktccy());
    }

    @ApiOperation(value = "每日一文", httpMethod = "GET", response = MeiRiYiWen.class)
    @GetMapping("/meiriyiwen")
    public Result<MeiRiYiWen> meiriyiwen(String date) throws Exception {
        return Result.success(apiService.meiriyiwen(date));
    }

    @ApiOperation(value = "检测百度收录", httpMethod = "GET", response = MeiRiYiWen.class)
    @GetMapping("/bd/iscollect")
    public Result<Boolean> iscollect(String url) throws Exception {
        return Result.success(apiService.isBaiduCollect(url));
    }
}
