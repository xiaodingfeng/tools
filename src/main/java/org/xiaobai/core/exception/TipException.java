package org.xiaobai.core.exception;


import org.xiaobai.core.enums.ErrorCodeEnum;

public class TipException extends RuntimeException {
    int code = 666;

    public TipException() {
        super();
    }

    public TipException(String msg) {
        super(msg);
    }

    public TipException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public TipException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMsg());
        this.code = errorCodeEnum.getCode();
    }
}
