package com.hzwl.rental.mapper.user;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hzwl.rental.entity.user.RentalCarImages;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author GA666666
 * @Date  2023/8/30 12:43
 */
@Mapper
public interface RentalCarImagesMapper  extends BaseMapper<RentalCarImages>{



    /** 
     * 分页查询指定行数据
     *
     * @param page 分页参数
     * @param wrapper 动态查询条件
     * @return 分页对象列表
     */
    IPage<RentalCarImages> selectByPage(IPage<RentalCarImages> page , @Param(Constants.WRAPPER) Wrapper<RentalCarImages> wrapper);
}