package org.xiaobai.ai.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaobai.ai.entity.SensitiveWordVO;
import org.xiaobai.ai.request.query.SensitiveWordPageQuery;
import org.xiaobai.ai.service.SensitiveWordService;
import org.xiaobai.tool.response.Result;

import javax.validation.Valid;

/**
 * 敏感词相关接口
 */
@AllArgsConstructor
@Api(value = "管理端-敏感词相关接口")
@RequestMapping("/chat/admin/sensitive_word")
@RestController
public class SensitiveWordAdminController {

    private final SensitiveWordService sensitiveWordService;

    @PostMapping("/page")
    @ApiOperation(value = "敏感词列表分页")
    public Result<IPage<SensitiveWordVO>> page(@Valid @RequestBody SensitiveWordPageQuery sensitiveWordPageQuery) {
        return Result.success(sensitiveWordService.pageSensitiveWord(sensitiveWordPageQuery));
    }
}
