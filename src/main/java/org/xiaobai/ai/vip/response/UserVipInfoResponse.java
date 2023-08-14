package org.xiaobai.ai.vip.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.xiaobai.ai.vip.entity.ChatVipUserVO;
import org.xiaobai.common.entity.SysUserVO;

import java.util.List;

/**
 * 用户会员信息
 */
@ApiModel(value = "用户会员信息")
@Data
public class UserVipInfoResponse {
    @ApiParam(value = "用户信息")
    private SysUserVO user;
    @ApiParam(value = "会员信息")
    private List<ChatVipUserVO> vipInfo;
}
