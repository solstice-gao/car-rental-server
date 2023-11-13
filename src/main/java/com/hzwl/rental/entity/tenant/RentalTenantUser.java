package com.hzwl.rental.entity.tenant;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author GA666666
 * @Date  2023/9/3 12:43
 */
@Data
@Builder
@ApiModel(value = "租户",description = "")
@TableName("rental_tenant_user")
@AllArgsConstructor
@NoArgsConstructor
public class RentalTenantUser implements Serializable,Cloneable{

    @ApiModelProperty(name = "租户号",notes = "")
    @TableId
    private String tenantId ;

    @ApiModelProperty(name = "租户名称",notes = "")
    private String tenantName ;

    @ApiModelProperty(name = "公司ID",notes = "")
    private String companyId ;

    @ApiModelProperty(name = "租户密码",notes = "")
    private String tenantPassword ;

    @ApiModelProperty(name = "租户角色",notes = "")
    private Integer tenantType ;

    @ApiModelProperty(name = "密码烟",notes = "")
    private String passSalt ;

    @ApiModelProperty(name = "租户状态",notes = "")
    private String tenantStatus ;

    @ApiModelProperty(name = "最后登录时间",notes = "")
    private Date lastLoginTime ;

    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createTime ;

    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateTime ;

    @TableField(exist = false)
    private Object token;

}
