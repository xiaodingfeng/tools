package org.xiaobai.common.response;

import lombok.Data;

@Data
public class TokenDTO {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
