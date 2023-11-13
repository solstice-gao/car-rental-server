package com.hzwl.rental.entity.dto;

import com.hzwl.rental.constants.PayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/9/9 23:06
 */
@Data
public class ReqCreatePreOrder {

    @NotNull
    @ApiModelProperty(name = "型号ID", notes = "")
    private String modelId;


    @NotNull
    @ApiModelProperty(name = "地址ID", notes = "")
    private String addressId;


    @ApiModelProperty(name = "优惠券ID", notes = "")
    private String couponId;


    @NotNull
    @ApiModelProperty(name = "订单类型", notes = "")
    private PayType orderType;


    @ApiModelProperty(name = "订单留言备注", notes = "")
    private String leaveComment;
}
