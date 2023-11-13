
package com.hzwl.rental.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Data
@ApiModel(value = "车辆信息", description = "")
@TableName("rental_cars")
public class RentalCars implements Serializable, Cloneable {

    @ApiModelProperty(name = "车辆ID", notes = "")
    @TableId
    private String carId;

    @NotNull
    @ApiModelProperty(name = "车辆描述", notes = "")
    private String carDesc;

    @ApiModelProperty(name = "型号ID", notes = "")
    private String modelId;

    @ApiModelProperty(name = "公司ID", notes = "")
    private String companyId;

    @ApiModelProperty(name = "门店ID", notes = "")
    private String storeId;

    @NotNull
    @ApiModelProperty(name = "车辆价格", notes = "")
    private BigDecimal price;

    @NotNull
    @ApiModelProperty(name = "描述标题", notes = "")
    private String carDescTitle;

    @NotNull
    @ApiModelProperty(name = "车辆发货地", notes = "")
    private String carSendFrom;

    @NotNull
    @ApiModelProperty(name = "备注", notes = "")
    private String carSendMarktext;

    @ApiModelProperty(name = "头图ID", notes = "")
    private String carImageId;

    @ApiModelProperty(name = "描述图片ID", notes = "")
    private String carImageDescId;

    @ApiModelProperty(name = "是否折扣", notes = "")
    private String discount;

    @ApiModelProperty(name = "车牌号", notes = "")
    private String licensePlate;

    @ApiModelProperty(name = "状态", notes = "", example = "1")
    private Integer status;

    @ApiModelProperty(name = "车辆效果图", notes = "")
    private String imageUrl;

    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createdTime;

    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updatedTime;

}