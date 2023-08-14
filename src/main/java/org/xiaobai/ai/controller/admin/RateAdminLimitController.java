package org.xiaobai.ai.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.RateLimitVO;
import org.xiaobai.ai.service.RateLimitService;
import org.xiaobai.tool.response.Result;

import java.util.List;

/**
 * 限流记录相关接口
 */
@AllArgsConstructor
@Api(value = "管理端-限流记录相关接口")
@RequestMapping("/chat/admin/rate_limit")
@RestController
public class RateAdminLimitController {

    private final RateLimitService rateLimitService;

    @ApiOperation(value = "限流列表")
    @PostMapping("/list")
    public Result<List<RateLimitVO>> listRateLimit() {
        return Result.success(rateLimitService.listRateLimit());
    }
}
