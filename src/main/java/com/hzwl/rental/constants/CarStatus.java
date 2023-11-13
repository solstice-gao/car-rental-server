package com.hzwl.rental.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author GA666666
 * @Date 2023/8/29 23:16
 */
@Getter
public enum CarStatus {

    NORMAL(1, "正常状态"),
    WAITINGRELEASE(-1, "待发布"),
    DISABLE(-2, "禁用状态"),
    PENDING_REVIEW(-3, "待审核"),
    ;


    private int status;
    private String desc;

    CarStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static CarStatus queryByCode(Integer code) {
        return Arrays.stream(CarStatus.values()).filter(carStatus -> carStatus.getStatus() == code).findFirst().orElse(null);
    }
}
