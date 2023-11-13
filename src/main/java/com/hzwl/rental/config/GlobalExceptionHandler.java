package com.hzwl.rental.config;


import cn.dev33.satoken.exception.NotLoginException;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author GA666666
 * @Date 2023/8/09 21:42
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleOtherExceptions(Exception e) {
        Result result = Result.error(ErrorCode.INTERNAL_CALL_ERROR.getErrorCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Result> handleBizExceptions(BizException e) {
        return ResponseEntity.status(HttpStatus.OK).body(Result.error(e.getErrorCode(), e.getErrorMsg()));
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Result> handleNotLoginExceptions(NotLoginException e) {
        return ResponseEntity.status(HttpStatus.OK).body(Result.error(ErrorCode.TOKEN_EXPIRATION.getErrorCode(), ErrorCode.TOKEN_EXPIRATION.getErrorMsg()));
    }
}
