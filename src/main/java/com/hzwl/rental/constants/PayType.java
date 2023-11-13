package com.hzwl.rental.constants;

import lombok.Getter;

/**
 * @Author GA666666
 * @Date 2023/9/9 23:09
 */
@Getter
public enum PayType {
    ALIPAY("ALIPAY", "支付宝"),
    WECHAT("WECHAT", "微信支付"),
    OTHER("OTHER", "其他支付"),
    ;


    private String code;
    private String desc;

    PayType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
