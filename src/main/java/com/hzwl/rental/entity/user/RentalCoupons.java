package com.hzwl.rental.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

 /**
 * @Author GA666666
 * @Date  2023/10/3 12:43
 */
@Data
@ApiModel(value = "优惠券",description = "")
@TableName("rental_coupons")
public class RentalCoupons implements Serializable,Cloneable{
    
    @ApiModelProperty(name = "id",notes = "")
    @TableId
    private String couponId ;
    
    @ApiModelProperty(name = "code",notes = "")
    private String couponCode ;
    
    @ApiModelProperty(name = "标题",notes = "")
    private String title ;
    
    @ApiModelProperty(name = "描述",notes = "")
    private String description ;
    
    @ApiModelProperty(name = "优惠方式：百分比折扣或固定金额折扣",notes = "")
    private String discountType ;
    
    @ApiModelProperty(name = "优惠金额或折扣百分比值",notes = "")
    private BigDecimal discountValue ;
    
    @ApiModelProperty(name = "最低购买金额限制",notes = "")
    private BigDecimal minimumPurchase ;
    
    @ApiModelProperty(name = "最大折扣金额限制",notes = "")
    private BigDecimal maximumDiscount ;
    
    @ApiModelProperty(name = "优惠券有效期开始日期",notes = "")
    private Date startDate ;
    
    @ApiModelProperty(name = "优惠券有效期结束日期",notes = "")
    private Date endDate ;
    
    @ApiModelProperty(name = "优惠券是否激活",notes = "")
    private String active ;
    
    @ApiModelProperty(name = "优惠券的最大使用次数限制",notes = "")
    private Integer maxUsage ;
    
    @ApiModelProperty(name = "优惠券已使用次数",notes = "")
    private Integer usedCount ;
    
    @ApiModelProperty(name = "优惠券创建时间",notes = "")
    private Date createdAt ;
    
    @ApiModelProperty(name = "优惠券更新时间",notes = "")
    private Date updatedAt ;

}