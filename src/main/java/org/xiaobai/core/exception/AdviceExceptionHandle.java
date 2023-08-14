package org.xiaobai.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.xiaobai.tool.response.Result;

import java.util.List;

@Slf4j
@RestControllerAdvice
@ResponseBody
public class AdviceExceptionHandle {
    @ExceptionHandler(TipException.class)
    public Result<String> tipExceptionAdvice(TipException e) {
        log.info(e.getMessage());
        return Result.fail(e.code, e.getMessage());
    }

    /**
     * 数据校验全局处理
     * MethodArgumentNotValidException @Valid使用校验失败时产生的异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> BindExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        StringBuilder errorMessage = new StringBuilder();
        List<ObjectError> objectErrors = methodArgumentNotValidException.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            for (int i = 0; i < objectErrors.size(); i++) {
                if (i != 0) {
                    errorMessage.append(",");
                }
                errorMessage.append(objectErrors.get(i).getDefaultMessage());
            }
        } else {
            errorMessage.append("MethodArgumentNotValidException occured.");
        }
        return Result.fail(400, errorMessage.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> tipExceptionAdvice(HttpMessageNotReadableException e) {
        log.info(e.getMessage());
        return Result.fail("请求参数异常");
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<String> tipExceptionAdvice(MaxUploadSizeExceededException e) {
        return Result.fail(666, "文件大小超过最大值");
    }

    @ExceptionHandler(Exception.class)
    public Result<String> tipExceptionAdvice(Exception e) {
        e.printStackTrace();
        return Result.fail(666, e.getMessage());
    }
}
