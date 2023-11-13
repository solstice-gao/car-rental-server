
package com.hzwl.rental.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Data
@ApiModel(value = "", description = "")
@TableName("rental_car_models")
public class RentalCarModels implements Serializable, Cloneable {

    @ApiModelProperty(name = "型号ID", notes = "")
    @TableId
    private String modelId;

    @ApiModelProperty(name = "车辆ID", notes = "")
    private String carId;

    @NotNull
    @ApiModelProperty(name = "车辆价格", notes = "", example = "1")
    private BigDecimal carPrice;

    @NotNull
    @ApiModelProperty(name = "首期费用", notes = "", example = "1")
    private BigDecimal carInitialFee;

    @NotNull
    @ApiModelProperty(name = "办理费", notes = "", example = "1")
    private BigDecimal carProcessingFee;

    @NotNull
    @ApiModelProperty(name = "电池费", notes = "", example = "1")
    private BigDecimal carBatteryFee;

    @NotNull
    @ApiModelProperty(name = "保险费", notes = "", example = "1")
    private BigDecimal carInsuranceFee;

    @NotNull
    @ApiModelProperty(name = "品牌", notes = "")
    private String brand;

    @NotNull
    @ApiModelProperty(name = "颜色", notes = "")
    private String color;

    @NotNull
    @ApiModelProperty(name = "型号", notes = "")
    private String model;

    @ApiModelProperty(name = "描述", notes = "")
    private String description;

    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createdTime;

    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updatedTime;

}