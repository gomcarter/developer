package com.gomcarter.frameworks.base.exception;

import com.gomcarter.frameworks.base.json.ErrorCode;

/**
 * 自定义异常类
 * @author gomcarter
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 6655164451288057079L;

    private Integer code = -1;

    public CustomException() {
        super();
    }

    public CustomException(ErrorCode code) {
        super(code.getMsg());
        this.code = code.getCode();
    }

    public CustomException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
