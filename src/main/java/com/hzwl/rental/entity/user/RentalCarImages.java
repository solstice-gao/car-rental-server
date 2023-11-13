
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
@ApiModel(value = "车辆图片",description = "")
@TableName("rental_car_images")
public class RentalCarImages implements Serializable,Cloneable{
    
    @ApiModelProperty(name = "图片ID",notes = "")
    @TableId
    private String imageId ;

    @ApiModelProperty(name = "车辆ID",notes = "")
    private String carId ;
    
    @ApiModelProperty(name = "车辆效果图",notes = "")
    private String imageUrl ;
    
    @ApiModelProperty(name = "图片描述",notes = "")
    private String imageDesc ;
    
    @ApiModelProperty(name = "状态",notes = "",example = "1")
    private Integer status ;
    
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createdTime ;
    
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updatedTime ;

}