
package com.hzwl.rental.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

 /**
 * @Author GA666666
 * @Date  2023/8/30 12:43
 */
 @Data
@ApiModel(value = "公司名称",description = "")
@TableName("rental_company")
public class RentalCompany implements Serializable,Cloneable{
    
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
    
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createdTime ;
    
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updatedTime ;

}