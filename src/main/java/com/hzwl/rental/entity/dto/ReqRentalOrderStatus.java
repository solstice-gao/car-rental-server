package com.hzwl.rental.entity.dto;

import lombok.Data;

@Data
public class ReqRentalOrderStatus {
    private String orderId;

    private Integer orderStatus;
}
