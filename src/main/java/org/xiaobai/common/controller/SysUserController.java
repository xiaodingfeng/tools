package org.xiaobai.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.common.request.LoginRequest;
import org.xiaobai.common.request.UpdateUserRequest;
import org.xiaobai.common.response.LoginResponse;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.tool.response.Result;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName SysUserController
 * @Description 用户接口
 * @Author dingfeng.xiao
 * @Date 2023/6/30 9:42
 * @Version 1.0
 */
@Api(value = "用户接口", tags = {"用户接口"})
@CrossOrigin
@RestController
@RequestMapping("/sys/user")
@Validated
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "用户登录", httpMethod = "POST", response = LoginResponse.class)
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return Result.success(sysUserService.login(loginRequest));
    }

    @ApiOperation(value = "用户注销", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        return Result.success(sysUserService.logout());
    }

    @ApiOperation(value = "用户发送验证码", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/captcha")
    public Result<Boolean> captcha(@RequestParam("email") @Valid @NotBlank(message = "邮箱不能为空") String email) {
        sysUserService.captcha(email);
        return Result.success();
    }

    @ApiOperation(value = "用户刷新验证码", httpMethod = "POST", response = LoginResponse.class)
    @PostMapping("/refreshToken")
    public Result<LoginResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return Result.success(sysUserService.refreshToken(refreshToken));
    }

    @ApiOperation(value = "获取用户信息", httpMethod = "GET", response = SysUserVO.class)
    @GetMapping("/getInfo")
    public Result<SysUserVO> getInfo() {
        Long userId = UserUtil.getUserId();
        if (userId == -1L) {
            SysUserVO userVO = new SysUserVO();
            userVO.setNickName("admin");
            userVO.setAvatar("https://fengzhengx.cn/upload/2021/01/logo-df2a3ea06ad34f39a31f04ec1ccce41d.png");
            return Result.success(userVO);
        }

        return Result.success(sysUserService.getById(userId));
    }

    @ApiOperation(value = "用户编辑信息", httpMethod = "POST", response = LoginResponse.class)
    @PostMapping("/updateUser")
    public Result<Boolean> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
        return Result.success(sysUserService.updateUser(UserUtil.getUserId(), updateUserRequest));
    }
}
