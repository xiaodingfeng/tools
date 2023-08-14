package org.xiaobai.im.response;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @ClassName ImUserResponse
 * @Description 聊天室用户信息
 * @Author dingfeng.xiao
 * @Date 2023/7/17 13:26
 * @Version 1.0
 */
@Data
public class ImUserResponse {
    @ApiParam(value = "userId")
    private Long userId;
    @ApiParam(value = "用户名")
    private String userName;
    @ApiParam(value = "昵称")
    private String nickName;
    @ApiParam(value = "是否在线")
    private Boolean isOnline;
}
