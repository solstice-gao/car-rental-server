package com.hzwl.rental.constants;

import lombok.Getter;

/**
 * @Author GA666666
 * @Date 2023/9/9 23:10
 */
@Getter
public enum OrderStatus {

    PENDING_PAYMENT(0, "待支付"), // 待支付
    PROCESSING(2, "Processing"),           // 处理中
    COMPLETED(1, "已支付"),             // 已完成
    CANCELLED(-1, "已取消"),             // 已取消
    REFUNDED(-2, "已退款");               // 已退款

    private final Integer status;
    private final String displayName; // 显示名称

    OrderStatus(int status, String displayName) {
        this.status = status;
        this.displayName = displayName;
    }

}
