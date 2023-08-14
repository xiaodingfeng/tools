package org.xiaobai.ai.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 限流处展示参数
 */
@Data
@Api(value = "限流处理器展示参数")
public class RateLimitVO {

    @ApiParam(value = "ip")
    private String ip;

    @ApiParam(value = "ip 限流规则")
    private String ipLimitRule;

    @ApiParam(value = "全局限流规则")
    private String globalLimitRule;

    @ApiParam(value = "是否被 ip 限流")
    private Boolean isIpLimited;

    @ApiParam(value = "是否被全局限流")
    private Boolean isGlobalLimited;

    @ApiParam(value = "ip 限制时间内已发送次数")
    private Integer alreadySendCount;

    @ApiParam(value = "下次可以发送消息的时间")
    private String nextSendTime;
}
