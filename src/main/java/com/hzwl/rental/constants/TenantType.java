package com.hzwl.rental.constants;

/**
 * @Author GA666666
 * @Date 2023/9/26 14:44
 */
public enum TenantType {
    NORMAL(1, "普通租户"),
    CANCEL(-1, "注销状态"),
    ADMIN(2, "管理员"),
    ;


    private int status;
    private String desc;

    TenantType(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
