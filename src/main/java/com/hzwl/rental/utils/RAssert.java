package com.hzwl.rental.utils;

import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @Author GA666666
 * @Date 2023/9/4 10:10
 */
public class RAssert {
    public RAssert() {
    }

    public static <X extends Throwable> void isTrue(boolean expression, ErrorCode errorCode) throws X {
        if (!expression) {
            throw new BizException(errorCode);
        }
    }

    public static <X extends Throwable> void nonNull(Object expression, ErrorCode errorCode) throws X {
        if (Objects.isNull(expression)) {
            throw new BizException(errorCode);
        }
    }
}
