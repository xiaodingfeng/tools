package org.xiaobai.controller;

import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.constants.KeyEnum;
import org.xiaobai.exception.TipException;
import org.xiaobai.request.LoginRequest;
import org.xiaobai.response.Result;
import org.xiaobai.utils.CacheUtil;

@RequestMapping
@RestController
public class LoginController {

    @Autowired
    private CacheUtil<String> cacheUtil;

    @RequestMapping("/login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        checkUser(loginRequest);
        String uuid = UUID.fastUUID().toString().replace("-", "");
        cacheUtil.put(KeyEnum.LOGIN_TOKEN.getValue() + uuid, loginRequest.getUserName());
        return Result.success(uuid);
    }

    @RequestMapping("/getUser")
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

        if (!loginRequest.getPassword().equals("12345678")) {
            throw new TipException("密码错误");
        }
    }
}
