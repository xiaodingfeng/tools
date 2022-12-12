package org.xiaobai.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaobai.response.Result;

@RestControllerAdvice
public class AdviceExceptionHandle {
    @ExceptionHandler(TipException.class)
    public Result<String> tipExceptionAdvice(TipException e) {
        return Result.fail(e.code, e.getMessage());
    }
}
