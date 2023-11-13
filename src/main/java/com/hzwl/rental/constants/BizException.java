package com.hzwl.rental.constants;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/8/09 21:42
 */
@Data
public class BizException extends RuntimeException {

    protected Integer errorCode;

    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
