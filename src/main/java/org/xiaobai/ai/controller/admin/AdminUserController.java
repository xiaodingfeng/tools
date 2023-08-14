package org.xiaobai.ai.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.common.request.SysUserPageQuery;
import org.xiaobai.common.response.LoginResponse;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(value = "用户信息", tags = {"用户信息"})
@CrossOrigin
@RestController
@RequestMapping("/chat/admin/user")
@Validated
public class AdminUserController {

    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "用户登录", httpMethod = "POST", response = LoginResponse.class)
    @PostMapping("/page")
    public Result<IPage<SysUserVO>> page(@RequestBody @Valid SysUserPageQuery pageQuery) {
        return Result.success(sysUserService.pageUser(pageQuery));
    }
}
