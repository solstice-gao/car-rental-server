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
 * @Date  2023/9/14 12:43
 */
@Data
@ApiModel(value = "提货地址",description = "")
@TableName("rental_delivery_address")
public class RentalDeliveryAddress implements Serializable,Cloneable{
    
    @ApiModelProperty(name = "主键",notes = "")
    @TableId
    private String deliveryId ;
    
    @ApiModelProperty(name = "",notes = "")
    private String carId ;
    
    @ApiModelProperty(name = "地址名称",notes = "")
    private String addressName ;
    
    @ApiModelProperty(name = "顺序号",notes = "")
    private Integer seqNumber ;
    
    @ApiModelProperty(name = "省",notes = "")
    private String province ;
    
    @ApiModelProperty(name = "市",notes = "")
    private String city ;
    
    @ApiModelProperty(name = "区",notes = "")
    private String county ;
    
    @ApiModelProperty(name = "街道",notes = "")
    private String street ;
    
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createTime ;
    
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateTime ;

}