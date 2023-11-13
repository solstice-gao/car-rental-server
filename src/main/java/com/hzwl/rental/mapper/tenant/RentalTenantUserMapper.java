package com.hzwl.rental.mapper.tenant;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hzwl.rental.entity.tenant.RentalTenantUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

 /**
 * @Author GA666666
 * @Date  2023/9/3 12:43
 */
@Mapper
public interface RentalTenantUserMapper  extends BaseMapper<RentalTenantUser>{
    /** 
     * 分页查询指定行数据
     *
     * @param page 分页参数
     * @param wrapper 动态查询条件
     * @return 分页对象列表
     */
    IPage<RentalTenantUser> selectByPage(IPage<RentalTenantUser> page , @Param(Constants.WRAPPER) Wrapper<RentalTenantUser> wrapper);
}