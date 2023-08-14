package org.xiaobai.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.common.request.LoginRequest;
import org.xiaobai.common.request.SysUserPageQuery;
import org.xiaobai.common.request.UpdateUserRequest;
import org.xiaobai.common.response.LoginResponse;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName SysUserService
 * @Description 用户管理
 * @Author dingfeng.xiao
 * @Date 2023/6/30 14:24
 * @Version 1.0
 */
public interface SysUserService extends IService<SysUserVO> {
    /**
     * 用户登录
     *
     * @param request
     * @return LoginResponse
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户退出
     */
    boolean logout();

    /**
     * 发送验证码
     *
     * @param email
     * @return boolean
     */
    void captcha(String email);

    /**
     * 刷新token
     *
     * @return
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 获取登录返回信息
     *
     * @param userId
     * @return
     */
    LoginResponse getLoginResponse(Long userId);

    /**
     * 用户信息分页
     *
     * @param pageQuery
     * @return SysUserVO
     */
    IPage<SysUserVO> pageUser(SysUserPageQuery pageQuery);

    /**
     * 生效的用户数据
     * @return
     */
    List<SysUserVO> listValid();

    /**
     * 更新用户信息
     * @param updateUserRequest
     */
    Boolean updateUser(Long userId, UpdateUserRequest updateUserRequest);
}
