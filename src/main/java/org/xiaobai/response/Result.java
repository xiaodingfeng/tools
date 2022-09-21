package org.xiaobai.response;

import lombok.Data;

/**
 * @author xdf
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result() {}

    public static <T> Result<T> success(String msg, T data) {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(200);
        apiResponse.setMsg(msg);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> Result<T> success() {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(200);
        return apiResponse;
    }

    public static <T> Result<T> success(T data) {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(200);
        apiResponse.setData(data);
        return apiResponse;
    }

    public Result<T> fail(String msg) {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(205);
        apiResponse.setMsg(msg);
        return apiResponse;
    }

    public Result<T> fail(int code, String msg) {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(code);
        apiResponse.setMsg(msg);
        return apiResponse;
    }
}
