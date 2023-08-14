package org.xiaobai.ai.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.service.ChatDrawAppService;

import javax.annotation.Resource;

/**
 * @ClassName DrawAppController
 * @Description 画图
 * @Author dingfeng.xiao
 * @Date 2023/6/30 15:45
 * @Version 1.0
 */
@Api(value = "画图", tags = {"画图"})
@CrossOrigin
@RestController
@RequestMapping("/chat/admin/drawApp")
@Validated
public class ChatAdminDrawAppController {

    @Resource
    private ChatDrawAppService chatDrawAppService;
    ///**
    // * 用户画图信息列表
    // */
    //@ApiOperation(value = "用户画图信息列表")
    //@PostMapping("/page")
    //public Result<Page<ChatDrawAppMyPageResponse>> myListPage(                                                              @RequestBody ChatDrawAppMyPageRequest drawAppMyPageRequest) {
    //    return Result.success(chatDrawAppService.page(userId, drawAppMyPageRequest));
    //}
}
