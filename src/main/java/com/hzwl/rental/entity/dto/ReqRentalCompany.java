package com.hzwl.rental.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class ReqRentalCompany {
    @Valid
    @NotBlank
    @ApiModelProperty(name = "公司名称",notes = "")
    private String companyName ;

    @Valid
    @NotBlank
    @ApiModelProperty(name = "公司服务城市",notes = "")
    private String companyCity ;

    @Valid
    @NotBlank
    @ApiModelProperty(name = "公司图片",notes = "")
    private String companyIcon ;

    @Valid
    @NotBlank
    @ApiModelProperty(name = "营业执照",notes = "")
    private String businessLicense;
}
