package org.xiaobai.common.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.xiaobai.ai.service.ChatUserBalanceService;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.common.enums.RedisKeyEnum;
import org.xiaobai.common.mapper.SysUserMapper;
import org.xiaobai.common.request.LoginRequest;
import org.xiaobai.common.request.SysUserPageQuery;
import org.xiaobai.common.request.UpdateUserRequest;
import org.xiaobai.common.response.LoginResponse;
import org.xiaobai.common.response.TokenDTO;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.enums.ErrorCodeEnum;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SysUserServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:25
 * @Version 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserVO> implements SysUserService {

    @Resource
    MailUtil mailUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ChatUserBalanceService chatUserBalanceService;

    @Override
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        String captcha = request.getCaptcha();
        String password = request.getPassword();
        // 优先验证码登录
        if (StrUtil.isNotEmpty(captcha)) {
            String captchaKey = RedisKeyEnum.USER_LOGIN_CAPTCHA.getSuffix() + email;
            String captchaTemp = redisUtil.get(captchaKey);
            if (!Objects.equals(captcha, captchaTemp)) {
                throw new TipException(ErrorCodeEnum.USER_LOGIN_CAPTCHA_FAIL);
            }
            redisUtil.delete(captchaKey);
        } else if (StrUtil.isNotEmpty(password)){
            SysUserVO sysUserVO = getOne(new LambdaQueryWrapper<SysUserVO>()
                    .eq(SysUserVO::getEmail, email));
            if (Objects.isNull(sysUserVO)) {
                throw new TipException("用户不存在");
            }
            if (!shaPassword(sysUserVO.getId(), password).equals(sysUserVO.getPassword())) {
                throw new TipException("密码错误");
            }
        } else {
            throw new TipException("参数异常");
        }

        Long userId = saveUser(email);
        return getLoginResponse(userId);
    }

    @Override
    public boolean logout() {
        try {
            String accessToken = UserUtil.getAccessToken();
            String tokenDTO = redisUtil.get(RedisKeyEnum.USER_ACCESS_TOKEN + accessToken);
            TokenDTO object = JSON.parseObject(tokenDTO, TokenDTO.class);
            redisUtil.delete(object.getRefreshToken());
            redisUtil.delete(object.getAccessToken());
        } catch (Exception e) {
            log.error("logout error:", e);
        }
        return true;
    }

    Long saveUser(String email) {
        if (count(new LambdaQueryWrapper<SysUserVO>()
                .eq(SysUserVO::getEmail, email)) == 0) {
            SysUserVO userVO = new SysUserVO();
            userVO.setEmail(email);
            save(userVO);
            userVO.setUserName("CHAT_" + userVO.getId());
            userVO.setNickName("CHAT_" + userVO.getId());
            userVO.setAvatar("https://fengzhengx.cn/upload/2021/01/logo-df2a3ea06ad34f39a31f04ec1ccce41d.png");
            userVO.setCreateTime(new Date());
            String randomPassword = RandomPwdUtil.randomPassword();
            userVO.setPassword(shaPassword(userVO.getId(), randomPassword));
            updateById(userVO);
            // 初始化额度信息
            if (!chatUserBalanceService.initUserBalance(userVO.getId())) {
                throw new TipException("初始化额度失败");
            }
            mailUtil.sendSimpleMail(email, "初始密码提示", "初始默认密码为：" + randomPassword);
            return userVO.getId();
        }
        return getOne(new LambdaQueryWrapper<SysUserVO>()
                .eq(SysUserVO::getEmail, email)).getId();
    }

    private String shaPassword(Long userId, String password) {
        return Base64Utils.encodeToString(DigestUtils.sha256(userId + password));
    }

    @Override
    public void captcha(String email) {
        String captchaKey = RedisKeyEnum.USER_LOGIN_CAPTCHA.getSuffix() + email;
        if (redisUtil.getExpire(captchaKey) > 60L) {
            throw new TipException(ErrorCodeEnum.USER_LOGIN_CAPTCHA_WAIT);
        }
        String verificationCode = VerificationCodeUtil.generateVerificationCode(6);
        mailUtil.sendSimpleMail(email, "登录验证码", "验证码为：" + verificationCode + "\n有效期为2分钟");
        redisUtil.setEx(RedisKeyEnum.USER_LOGIN_CAPTCHA.getSuffix() + email, verificationCode, 120L, TimeUnit.SECONDS);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        String tempTokenKey = RedisKeyEnum.USER_REFRESH_TOKEN.getSuffix() + refreshToken;
        String tokenDTO = redisUtil.get(tempTokenKey);
        if (StrUtil.isEmpty(tokenDTO)) {
            throw new TipException(ErrorCodeEnum.REFRESH_TOKEN_NOT_EXITS);
        }
        TokenDTO object = JSON.parseObject(tokenDTO, TokenDTO.class);
        LoginResponse loginResponse = getLoginResponse(Long.valueOf(object.getUserId()));
        redisUtil.delete(tempTokenKey);
        redisUtil.delete(object.getAccessToken());
        return loginResponse;
    }

    @Override
    public LoginResponse getLoginResponse(Long userId) {
        String accessToken = UUID.fastUUID().toString().replace("-", "");
        String refreshToken = UUID.fastUUID().toString().replace("-", "");
        TokenDTO token = new TokenDTO();
        token.setUserId(String.valueOf(userId));
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        redisUtil.setEx(RedisKeyEnum.USER_ACCESS_TOKEN.getSuffix() + accessToken, JSON.toJSONString(token), 7200L, TimeUnit.SECONDS);
        redisUtil.setEx(RedisKeyEnum.USER_REFRESH_TOKEN.getSuffix() + refreshToken, JSON.toJSONString(token), 7L, TimeUnit.DAYS);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setAccessTokenExpireTime(7200L);
        // 30天
        loginResponse.setRefreshTokenExpireTime(7200L * 12 * 30);
        return loginResponse;
    }

    @Override
    public IPage<SysUserVO> pageUser(SysUserPageQuery pageQuery) {
        IPage<SysUserVO> sysUserPage = page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()),
                new LambdaQueryWrapper<>());

        return PageUtil.toPage(sysUserPage, sysUserPage.getRecords());
    }

    @Override
    public List<SysUserVO> listValid() {
        return list();
    }

    @Override
    public Boolean updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        SysUserVO userVO = getById(userId);
        userVO.setNickName(updateUserRequest.getNickName());
        userVO.setEmail(updateUserRequest.getEmail());
        userVO.setAvatar(updateUserRequest.getAvatar());
        if (StrUtil.isNotEmpty(updateUserRequest.getPassword())) {
            userVO.setPassword(shaPassword(userId, updateUserRequest.getPassword()));
        }
        return updateById(userVO);
    }
}
