package org.xiaobai.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName DatetypeEnum
 * @Description 日期类型
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:20
 * @Version 1.0
 */
@AllArgsConstructor
public enum DateTypeEnum {
    /**
     * SECOND
     */
    SECOND("SECOND", "秒"),
    /**
     * hour
     */
    MINUTE("MINUTE", "分钟"),
    /**
     * HOUR
     */
    HOUR("HOUR", "小时"),
    /**
     * DAY
     */
    DAY("DAY", "天"),
    /**
     * WEEK
     */
    WEEK("WEEK", "周"),
    /**
     * MONTH
     */
    MONTH("MONTH", "月"),
    /**
     * YEAR
     */
    YEAR("YEAR", "年");
    @Getter
    @EnumValue
    private final String type;

    @Getter
    @JsonValue
    private final String message;
}
