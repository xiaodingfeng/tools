package org.xiaobai.ai.controller.admin;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.common.request.LoginRequest;
import org.xiaobai.common.response.LoginResponse;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.core.config.ChatConfig;
import org.xiaobai.core.enums.ErrorCodeEnum;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

@Api(value = "后台登录", tags = {"后台登录"})
@CrossOrigin
@RestController
@RequestMapping("/chat/admin")
@Validated
public class AdminLoginController {
    @Resource
    private ChatConfig chatConfig;

    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "用户登录", httpMethod = "POST", response = LoginResponse.class)
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        String adminAccount = chatConfig.getAdminAccount();
        String adminPassword = chatConfig.getAdminPassword();
        if (StrUtil.isNotEmpty(adminAccount) && StrUtil.isNotEmpty(adminPassword)) {
            if (!Objects.equals(loginRequest.getEmail(), adminAccount) || !Objects.equals(loginRequest.getPassword(), adminPassword)) {
                throw new TipException(ErrorCodeEnum.ADMIN_LOGIN_FAIL);
            }
        }
        Long adminId = -1L;
        LoginResponse loginResponse = sysUserService.getLoginResponse(adminId);
        return Result.success(loginResponse);
    }
}
