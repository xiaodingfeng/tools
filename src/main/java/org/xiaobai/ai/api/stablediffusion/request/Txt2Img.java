package org.xiaobai.ai.api.stablediffusion.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName Txt2Img
 * @Description 文字生图
 * @Author dingfeng.xiao
 * @Date 2023/7/7 14:19
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class Txt2Img {

    @JsonProperty("enable_hr")
    private Boolean enableHr;
    @JsonProperty("denoising_strength")
    private Integer denoisingStrength;
    @JsonProperty("firstphase_width")
    private Integer firstphaseWidth;
    @JsonProperty("firstphase_height")
    private Integer firstphaseHeight;
    @JsonProperty("hr_scale")
    private Integer hrScale;
    @JsonProperty("hr_upscaler")
    private String hrUpscaler;
    @JsonProperty("hr_second_pass_steps")
    private Integer hrSecondPassSteps;
    @JsonProperty("hr_resize_x")
    private Integer hrResizeX;
    @JsonProperty("hr_resize_y")
    private Integer hrResizeY;
    @JsonProperty("prompt")
    private String prompt;
    @JsonProperty("seed")
    private Integer seed;
    @JsonProperty("subseed")
    private Integer subseed;
    @JsonProperty("subseed_strength")
    private Integer subseedStrength;
    @JsonProperty("seed_resize_from_h")
    private Integer seedResizeFromH;
    @JsonProperty("seed_resize_from_w")
    private Integer seedResizeFromW;
    @JsonProperty("batch_size")
    private Integer batchSize;
    @JsonProperty("n_iter")
    private Integer nIter;
    @JsonProperty("steps")
    private Integer steps;
    @JsonProperty("cfg_scale")
    private Integer cfgScale;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("restore_faces")
    private Boolean restoreFaces;
    @JsonProperty("tiling")
    private Boolean tiling;
    @JsonProperty("do_not_save_samples")
    private Boolean doNotSaveSamples;
    @JsonProperty("do_not_save_grid")
    private Boolean doNotSaveGrid;
    @JsonProperty("negative_prompt")
    private String negativePrompt;
    @JsonProperty("eta")
    private Integer eta;
    @JsonProperty("s_min_uncond")
    private Integer sMinUncond;
    @JsonProperty("s_churn")
    private Integer sChurn;
    @JsonProperty("s_tmax")
    private Integer sTmax;
    @JsonProperty("s_tmin")
    private Integer sTmin;
    @JsonProperty("s_noise")
    private Integer sNoise;
    @JsonProperty("override_settings")
    private OverrideSettingsDTO overrideSettings;
    @JsonProperty("override_settings_restore_afterwards")
    private Boolean overrideSettingsRestoreAfterwards;
    @JsonProperty("script_args")
    private List<Integer> scriptArgs;
    @JsonProperty("sampler_index")
    private String samplerIndex;
    @JsonProperty("send_images")
    private Boolean sendImages;
    @JsonProperty("save_images")
    private Boolean saveImages;
    @JsonProperty("alwayson_scripts")
    private AlwaysonScriptsDTO alwaysonScripts;

    @NoArgsConstructor
    @Data
    public static class OverrideSettingsDTO {
        @JsonProperty("sd_model_checkpoint")
        private String sdModelCheckpoint;
    }

    @NoArgsConstructor
    @Data
    public static class AlwaysonScriptsDTO {
    }
}
