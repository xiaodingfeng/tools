package org.xiaobai.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}