package com.hzwl.rental.mapper.user;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hzwl.rental.entity.user.RentalCarAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author GA666666
 * @Date 2023/9/27 10:35
 */
@Mapper
public interface RentalCarAddressMapper extends BaseMapper<RentalCarAddress> {
    /**
     * 分页查询指定行数据
     *
     * @param page    分页参数
     * @param wrapper 动态查询条件
     * @return 分页对象列表
     */
    IPage<RentalCarAddress> selectByPage(IPage<RentalCarAddress> page, @Param(Constants.WRAPPER) Wrapper<RentalCarAddress> wrapper);
}
