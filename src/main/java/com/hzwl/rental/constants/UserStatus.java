package com.hzwl.rental.constants;

import lombok.Getter;

/**
 * @Author GA666666
 * @Date 2023/8/29 23:16
 */
@Getter
public enum UserStatus {

    NORMAL(1, "正常状态"),
    CANCEL(-1, "注销状态"),
    DISABLE(-2, "禁用状态"),

    ;


    private int status;
    private String desc;

    UserStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
