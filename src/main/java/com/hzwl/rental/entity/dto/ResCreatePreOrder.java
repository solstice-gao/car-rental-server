package com.hzwl.rental.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/9/9 23:06
 */
@Data
public class ResCreatePreOrder {

    @ApiModelProperty(name = "型号ID", notes = "")
    private String orderId;

    @ApiModelProperty(name = "支付网址", notes = "")
    private String link;
}
