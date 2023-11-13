
package com.hzwl.rental.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Data
@ApiModel(value = "用户地址", description = "")
@TableName("rental_user_address")
public class RentalUserAddress implements Serializable, Cloneable {

    @ApiModelProperty(name = "地址ID", notes = "")
    @TableId
    private String addressId;

    @ApiModelProperty(name = "用户ID", notes = "")
    private String userId;

    @NotNull(message = "userName is required")
    @ApiModelProperty(name = "名称", notes = "")
    private String userName;

    @NotNull(message = "phone is required")
    @ApiModelProperty(name = "手机号", notes = "")
    private String phone;

    @ApiModelProperty(name = "顺序号", notes = "")
    private Integer seqNumber;

    @NotNull(message = "province is required")
    @ApiModelProperty(name = "省", notes = "")
    private String province;

    @NotNull(message = "city is required")
    @ApiModelProperty(name = "市", notes = "")
    private String city;

    @NotNull(message = "county is required")
    @ApiModelProperty(name = "区", notes = "")
    private String county;

    @NotNull(message = "street is required")
    @ApiModelProperty(name = "街道", notes = "")
    private String street;

    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createTime;

    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updateTime;

}