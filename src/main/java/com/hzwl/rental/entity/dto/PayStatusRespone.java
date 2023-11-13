package com.hzwl.rental.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author GA666666
 * @Date 2023/9/10 10:33
 */
@Data
public class PayStatusRespone {

    @ApiModelProperty(name = "订单号", notes = "")
    private String id;


    @ApiModelProperty(name = "支付状态, 0:待支付, 1:已支付, -1:已取消, 2:已退款", notes = "")
    private Integer status;


    @ApiModelProperty(name = "金额(分)", notes = "")
    private BigDecimal amount;


    @ApiModelProperty(name = "支付链接", notes = "")
    private String link;


    @ApiModelProperty(name = "验签, md5(amount + id + link + status + secret)", notes = "")
    private String sign;
}
