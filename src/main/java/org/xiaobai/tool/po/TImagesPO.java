package org.xiaobai.tool.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("`T_IMAGES`")
public class TImagesPO extends BaseIdPO {
    private String category;
    private String description;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "real_file_name")
    private String realFileName;
    @TableField(value = "real_url")
    private String realUrl;
    private String url;
    private String type;
    private String poster;
    private String size;
    private String md5;
    private String pixel;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
