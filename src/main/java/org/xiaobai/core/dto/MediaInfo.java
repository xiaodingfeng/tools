package org.xiaobai.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MediaInfo
 * @Description TODO
 * @Author dingfeng.xiao
 * @Date 2023/8/8 16:12
 * @Version 1.0
 */
@Data
public class MediaInfo {
    @Data
    public static class Format {
        @JsonProperty("bit_rate")
        private String bitRate;
    }

    @Data
    public static class Stream {
        @JsonProperty("index")
        private int index;

        @JsonProperty("codec_name")
        private String codecName;

        @JsonProperty("codec_long_name")
        private String codecLongame;

        @JsonProperty("profile")
        private String profile;
    }

    @JsonProperty("streams")
    private List<Stream> streams;

    @JsonProperty("format")
    private Format format;
}
