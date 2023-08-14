package org.xiaobai.ai.response;

import lombok.Data;

/**
 * @ClassName DayBalanceResonse
 * @Author dingfeng.xiao
 * @Date 2023/7/12 10:57
 * @Version 1.0
 */
@Data
public class DayBalanceResponse {
    /**
     * 额度
     */
    private Integer balance;
    /**
     * 能否被领取
     */
    private Boolean canBeReceive;
}
