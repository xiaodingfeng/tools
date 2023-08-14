package org.xiaobai.tool.controller;

import cn.hutool.core.lang.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.CacheUtil;
import org.xiaobai.tool.constants.KeyEnum;
import org.xiaobai.tool.request.LoginRequest;
import org.xiaobai.tool.response.Result;

@Api(value = "登录", tags = {"登录"})
@RequestMapping("/tool")
@RestController
public class LoginController {

    @Autowired
    private CacheUtil<String> cacheUtil;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        checkUser(loginRequest);
        String uuid = UUID.fastUUID().toString().replace("-", "");
        cacheUtil.put(KeyEnum.LOGIN_TOKEN.getValue() + uuid, loginRequest.getUserName());
        return Result.success(uuid);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result<Boolean> logout(@RequestHeader("authorization") String token) {
        cacheUtil.remove(KeyEnum.LOGIN_TOKEN.getValue() + token.replace("bearer ", ""));
        return Result.success(true);
    }

    @ApiOperation("获取用户信息")
    @PostMapping("/getUser")
    public Result<String> getUser(@RequestHeader("authorization") String token) {
        String user = cacheUtil.get(KeyEnum.LOGIN_TOKEN.getValue() + token.replace("bearer ", ""));
        return Result.success(user);
    }

    private void checkUser(LoginRequest loginRequest) {
        if (!StringUtils.hasText(loginRequest.getUserName())) {
            throw new TipException(403, "用户名不为空");
        }
        if (!StringUtils.hasText(loginRequest.getPassword())) {
            throw new TipException(403, "密码不为空");
        }

        if (!loginRequest.getPassword().equals("123456")) {
            throw new TipException("密码错误");
        }
    }
}
