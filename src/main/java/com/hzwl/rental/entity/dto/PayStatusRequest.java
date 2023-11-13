package com.hzwl.rental.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/9/10 10:33
 */
@Data
public class PayStatusRequest {

    @ApiModelProperty(name = "订单号", notes = "")
    private String orderId;


    @ApiModelProperty(name = "业务单号, 二选一传递即可", notes = "")
    private String businessId;

}
