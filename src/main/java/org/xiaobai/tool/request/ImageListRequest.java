package org.xiaobai.tool.request;

import lombok.Data;

@Data
public class ImageListRequest {
    private int pageNum = 1;
    private int pageSize = 20;
    private String category;
    private String keywords;
}
