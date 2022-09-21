package org.xiaobai.response;

import lombok.Data;

import java.util.Date;

@Data
public class BingImage {
    private String title;
    private String description;
    private String image;
    private Date date;
}
