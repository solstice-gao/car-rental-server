package com.hzwl.rental.constants;

import lombok.Getter;

/**
 * @Author GA666666
 * @Date 2023/8/29 23:16
 */
@Getter
public enum ImageStatus {

    HEADER_IMAGES(1, "头图"),
    DESC_IMAGES(2, "详情图"),
    BUSINESS_IMAGES(3, "营业执照"),
    COMPANY_IMAGES(4, "公司图标"),
    CAROUSEL_IMAGES(5, "首页轮播图"),

    ;


    private int status;
    private String desc;

    ImageStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
