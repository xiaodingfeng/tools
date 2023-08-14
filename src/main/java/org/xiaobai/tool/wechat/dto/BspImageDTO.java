package org.xiaobai.tool.wechat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BspImageDTO {

    @JsonProperty("total")
    private Integer total;
    @JsonProperty("hasMore")
    private Boolean hasMore;
    @JsonProperty("rows")
    private List<RowsDTO> rows;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("pagination")
    private PaginationDTO pagination;

    @NoArgsConstructor
    @Data
    public static class PaginationDTO {
        @JsonProperty("pageIndex")
        private Integer pageIndex;
        @JsonProperty("pageSize")
        private Integer pageSize;
    }

    @NoArgsConstructor
    @Data
    public static class RowsDTO {
        @JsonProperty("_id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("mode")
        private Integer mode;
        @JsonProperty("category_id")
        private String categoryId;
        @JsonProperty("tags")
        private String tags;
        @JsonProperty("images")
        private List<String> images;
        @JsonProperty("view")
        private Integer view;
        @JsonProperty("hot")
        private Integer hot;
        @JsonProperty("verify")
        private Integer verify;
        @JsonProperty("status")
        private Integer status;
        @JsonProperty("_add_time")
        private Long addTime;
        @JsonProperty("_add_time_str")
        private String addTimeStr;
    }
}
