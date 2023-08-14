package org.xiaobai.core.config;

import lombok.Data;

/**
 * @ClassName TranscodeConfig
 * @Description TranscodeConfig
 * @Author dingfeng.xiao
 * @Date 2023/8/8 16:11
 * @Version 1.0
 */
@Data
public class TranscodeConfig {

    private String poster = "00:00:00.001";				// 截取封面的时间			HH:mm:ss.[SSS]
    private String tsSeconds = "2";			// ts分片大小，单位是秒
    private String cutStart;			// 视频裁剪，开始时间		HH:mm:ss.[SSS]
    private String cutEnd;				// 视频裁剪，结束时间		HH:mm:ss.[SSS]
}
