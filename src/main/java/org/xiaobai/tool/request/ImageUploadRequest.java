package org.xiaobai.tool.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ImageUploadRequest {
    private String description;
    private String category;
    private MultipartFile[] file;
}

