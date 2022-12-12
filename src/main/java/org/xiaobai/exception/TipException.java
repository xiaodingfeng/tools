package org.xiaobai.exception;


public class TipException extends RuntimeException {
    int code;
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
}
