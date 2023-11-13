package com.hzwl.rental.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/8/29 12:43
 */
@Data
@ApiModel(value = "订单", description = "")
@TableName("rental_order")
public class RentalOrder implements Serializable, Cloneable {

    @ApiModelProperty(name = "订单ID", notes = "")
    @TableId
    private String orderId;

    @ApiModelProperty(name = "订单No", notes = "")
    private String orderNo;

    @ApiModelProperty(name = "第三方订单ID", notes = "")
    private String otherOrderId;

    @ApiModelProperty(name = "支付连接", notes = "")
    private String link;

    @ApiModelProperty(name = "用户ID", notes = "")
    private String userId;

    @ApiModelProperty(name = "车ID", notes = "")
    private String carId;

    @ApiModelProperty(name = "车modelId", notes = "")
    private String modelId;

    @ApiModelProperty(name = "公司名称", notes = "")
    private String companyId;

    @ApiModelProperty(name = "地址ID", notes = "")
    private String addressId;

    @ApiModelProperty(name = "优惠券ID", notes = "")
    private String couponId;

    @ApiModelProperty(name = "总金额", notes = "", example = "1")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "优惠金额", notes = "", example = "1")
    private BigDecimal couponPrice;

    @ApiModelProperty(name = "应付金额", notes = "", example = "1")
    private BigDecimal payablePrice;

    @ApiModelProperty(name = "支付方式", notes = "")
    private String payMethod;

    @ApiModelProperty(name = "开票设置ID", notes = "")
    private String invoiceTplId;

    @ApiModelProperty(name = "订单留言备注", notes = "")
    private String leaveComment;

    @ApiModelProperty(name = "订单状态", notes = "", example = "1")
    private Integer orderStatus;

    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createdTime;

    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updatedTime;

    @TableField(exist = false)
    public String phone;

}