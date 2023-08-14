package org.xiaobai.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExportTypeEnum {
    MARK_DOWN("MD", "markdown"),
    IMAGE("IMG", "image");
    @Getter
    @EnumValue
    private final String type;

    @Getter
    @JsonValue
    private final String message;
}
