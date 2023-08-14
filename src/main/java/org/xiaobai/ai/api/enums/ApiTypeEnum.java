package org.xiaobai.ai.api.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 类型枚举
 */
@AllArgsConstructor
public enum ApiTypeEnum {

    /**
     * API_KEY
     */
    API_KEY("ApiKey", "ChatGPTAPI"),

    /**
     * ACCESS_TOKEN
     */
    ACCESS_TOKEN("AccessToken", "ChatGPTUnofficialProxyAPI"),

    /**
     * stable diffusion
     */
    STABLE_DIFFUSION("stableDiffusion", "ImgAPI");

    @Getter
    @EnumValue
    private final String name;

    @Getter
    @JsonValue
    private final String message;
}
