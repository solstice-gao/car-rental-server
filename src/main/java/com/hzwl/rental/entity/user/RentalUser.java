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
 * @Date 2023/8/29 10:43
 */
@Data
@ApiModel(value = "用户", description = "")
@TableName("rental_user")
public class RentalUser implements Serializable, Cloneable {

    @ApiModelProperty(name = "用户ID", notes = "1")
    @TableId
    private String userId;

    @ApiModelProperty(name = "用户名称", notes = "1")
    private String username;

    @ApiModelProperty(name = "用户昵称", notes = "1")
    private String nickname;

    @ApiModelProperty(name = "个性签名", notes = "")
    private String userIntro;

    @ApiModelProperty(name = "头像图片", notes = "")
    private String avatar;

    @ApiModelProperty(name = "邮件地址", notes = "")
    private String email;

    @ApiModelProperty(name = "手机号", notes = "")
    private String phone;

    @ApiModelProperty(name = "密码", notes = "")
    private String userPass;

    @ApiModelProperty(name = "密码盐", notes = "")
    private String passSalt;

    @ApiModelProperty(name = "用户状态", notes = "",example = "1")
    private Integer userStatus;

    @ApiModelProperty(name = "用户打分", notes = "",example = "1")
    private Integer userScore;

    @ApiModelProperty(name = "累计消费金额", notes = "",example = "1")
    private BigDecimal totalCostAmt;

    @ApiModelProperty(name = "最后登录时间", notes = "")
    private Date lastLoginTime;

    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createdTime;

    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updatedTime;

}