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
 * @Date  2023/10/10 12:43
 */
@Data
@ApiModel(value = "首页轮播图",description = "")
@TableName("rental_carousel")
public class RentalCarousel implements Serializable,Cloneable{
    
    @ApiModelProperty(name = "图片ID",notes = "")
    @TableId
    private String carouselId ;
    
    @ApiModelProperty(name = "展示地区",notes = "")
    private String showCity ;
    
    @ApiModelProperty(name = "效果图",notes = "")
    private String imageUrl ;
    
    @ApiModelProperty(name = "状态",notes = "",example = "1")
    private Integer status ;
    
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createdTime ;
    
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updatedTime ;

}