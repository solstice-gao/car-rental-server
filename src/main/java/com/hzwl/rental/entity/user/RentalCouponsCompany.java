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
 * @Date  2023/10/3 12:43
 */
@Data
@ApiModel(value = "优惠券可用公司",description = "")
@TableName("rental_coupons_company")
public class RentalCouponsCompany implements Serializable,Cloneable{
    
    @ApiModelProperty(name = "主键",notes = "")
    @TableId
    private String companyCouponsId ;
    
    @ApiModelProperty(name = "优惠券ID",notes = "")
    private String couponsId ;
    
    @ApiModelProperty(name = "公司ID",notes = "")
    private String companyId ;
    
    @ApiModelProperty(name = "状态",notes = "")
    private Integer status ;
    
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createdTime ;

}