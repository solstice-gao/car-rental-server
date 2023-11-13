package com.hzwl.rental.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hzwl.rental.entity.user.RentalCars;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "公司名称",description = "")
public class ResGroupByCompany {
    @ApiModelProperty(name = "公司ID",notes = "")
    @TableId
    private String companyId ;

    @ApiModelProperty(name = "地址",notes = "")
    private String addressId ;

    @ApiModelProperty(name = "公司名称",notes = "")
    private String companyName ;

    @ApiModelProperty(name = "公司服务城市",notes = "")
    private String companyCity ;

    @ApiModelProperty(name = "公司月销量",notes = "")
    private String companyMonthlySales ;

    @ApiModelProperty(name = "公司图片",notes = "")
    private String companyIcon ;

    @ApiModelProperty(name = "营业执照",notes = "")
    private String businessLicense;


    @ApiModelProperty(name = "车辆信列表",notes = "")
    private List<ResRentalCars> carList;
}
