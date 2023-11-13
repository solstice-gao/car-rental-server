package com.hzwl.rental.constants;

import lombok.Getter;

/**
 * @Author GA666666
 * @Date 2023/10/3 16:09
 */
@Getter
public enum CouponsStatus {
    NORMAL(1, "正常状态"),
    USED(-1, "已使用"),
    DISABLE(-2, "禁用状态"),

    ;


    private int status;
    private String desc;

    CouponsStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
