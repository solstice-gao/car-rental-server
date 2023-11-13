package com.hzwl.rental.constants;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CarouselStatus {
    NORMAL(1, "正常状态"),
    WAITINGRELEASE(-1, "待发布"),
    DEFAULT(2, "兜底"),
    ;


    private int status;
    private String desc;

    CarouselStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static CarouselStatus queryByCode(Integer code) {
        return Arrays.stream(CarouselStatus.values()).filter(carStatus -> carStatus.getStatus() == code).findFirst().orElse(null);
    }
}
