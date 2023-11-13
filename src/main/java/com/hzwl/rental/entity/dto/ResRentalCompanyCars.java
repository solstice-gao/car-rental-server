package com.hzwl.rental.entity.dto;

import com.hzwl.rental.entity.user.RentalCars;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:50
 */
@Data
public class ResRentalCompanyCars {

    @ApiModelProperty(name = "公司ID",notes = "")
    private String companyId;

    @ApiModelProperty(name = "公司名称",notes = "")
    private String companyName;

    @ApiModelProperty(name = "公司图片",notes = "")
    private String companyIcon ;

    @ApiModelProperty(name = "车辆信息",notes = "")
    private List<RentalCars> carList;
}
