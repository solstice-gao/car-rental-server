package com.hzwl.rental.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/9/4 15:16
 */
@Data
public class ReqRentalTenantUser {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
