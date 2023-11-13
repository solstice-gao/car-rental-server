package com.hzwl.rental.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Data
@ApiModel(value = "优惠券记录", description = "")
@TableName("rental_coupons_record")
public class RentalCouponsRecord implements Serializable, Cloneable {

    @ApiModelProperty(name = "", notes = "")
    @TableId
    private String recordId;

    @ApiModelProperty(name = "", notes = "")
    private String couponsId;

    @ApiModelProperty(name = "", notes = "")
    private String userId;

    @ApiModelProperty(name = "", notes = "")
    private Date couponsExpired;

    @ApiModelProperty(name = "", notes = "", example = "1")
    private Integer couponsStatus;

    @ApiModelProperty(name = "", notes = "")
    private Date createTime;

}