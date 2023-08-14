package org.xiaobai.tool.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xdf
 */
@Data
@ApiModel("通用响应")
public class Result<T> {
    @ApiModelProperty("返回码")
    private int code;
    @ApiModelProperty("信息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;

    private Result() {
    }

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

    public static <T> Result<T> fail(String msg) {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(205);
        apiResponse.setMsg(msg);
        return apiResponse;
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> apiResponse = new Result<>();
        apiResponse.setCode(code);
        apiResponse.setMsg(msg);
        return apiResponse;
    }
}
