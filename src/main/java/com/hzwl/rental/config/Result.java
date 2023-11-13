package com.hzwl.rental.config;

import com.hzwl.rental.constants.ErrorCode;
import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/8/09 21:42
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;


    public static <T> Result<T> success() {
        Result<T> response = new Result<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(null);
        return response;
    }

    public static <T> Result<T> success(T data) {
        Result<T> response = new Result<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(data);
        return response;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> response = new Result<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        Result<T> response = new Result<>();
        response.setCode(errorCode.getErrorCode());
        response.setMessage(errorCode.getErrorMsg());
        return response;
    }

}
