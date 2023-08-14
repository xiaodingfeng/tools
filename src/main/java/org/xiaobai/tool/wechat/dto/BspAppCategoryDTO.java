package org.xiaobai.tool.wechat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BspAppCategoryDTO {

    @JsonProperty("msg")
    private String msg;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("bzcate")
    private CateDTO bzcate;
    @JsonProperty("txcate")
    private CateDTO txcate;
    @JsonProperty("tags")
    private CateDTO tags;

    @NoArgsConstructor
    @Data
    public static class CateDTO {
        @JsonProperty("msg")
        private String msg;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("code")
        private Integer code;
        @JsonProperty("pagination")
        private PaginationDTO pagination;
        @JsonProperty("hasMore")
        private Boolean hasMore;
        @JsonProperty("rows")
        private List<RowsDTO> rows;

        @NoArgsConstructor
        @Data
        public static class RowsDTO {
            @JsonProperty("mode")
            private Integer mode;
            @JsonProperty("image")
            private String image;
            @JsonProperty("_add_time_str")
            private String addTimeStr;
            @JsonProperty("_add_time")
            private Long addTime;
            @JsonProperty("name")
            private String name;
            @JsonProperty("_id")
            private String id;
            @JsonProperty("hot")
            private Integer hot;
            @JsonProperty("hname")
            private String hname;
        }

        @NoArgsConstructor
        @Data
        public static class PaginationDTO {
            @JsonProperty("pageIndex")
            private Integer pageIndex;
            @JsonProperty("pageSize")
            private Integer pageSize;
        }
    }
}
