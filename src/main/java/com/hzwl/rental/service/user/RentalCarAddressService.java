package com.hzwl.rental.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import com.hzwl.rental.entity.user.RentalCarAddress;
import com.hzwl.rental.mapper.user.RentalCarAddressMapper;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Slf4j
@Service
public class RentalCarAddressService {

    @Autowired
    private RentalCarAddressMapper rentalCarAddressMapper;

    @Autowired
    private UserUtils userUtils;

    /**
     * 通过ID查询单条数据
     *
     * @param addressId 主键
     * @return 实例对象
     */
    public RentalCarAddress queryById(String addressId) {
        return rentalCarAddressMapper.selectById(addressId);
    }

    public List<RentalCarAddress> queryByCarId(String carId) {
        QueryWrapper<RentalCarAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", carId);
        return rentalCarAddressMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param rentalCarAddress 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCarAddress> queryEquities(RentalCarAddress rentalCarAddress, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalCarAddress> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCarAddress> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalCarAddress.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalCarAddress);
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    queryWrapper.like(StrUtil.toUnderlineCase(field.getName()), value);
                }
            } catch (IllegalAccessException e) {
                log.error("Error accessing field", e);
            }
        }
        if (startTime != null && endTime != null) {
            queryWrapper.between("create_time", startTime, endTime);
        }
        Page<RentalCarAddress> resultPage = rentalCarAddressMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalCarAddress 实例对象
     * @return 实例对象
     */
    public RentalCarAddress insert(RentalCarAddress rentalCarAddress) {
        rentalCarAddress.setAddressId(null);
        rentalCarAddress.setCarId(rentalCarAddress.getCarId());
        rentalCarAddress.setCreateTime(new Date());
        rentalCarAddress.setUpdateTime(new Date());
        rentalCarAddressMapper.insert(rentalCarAddress);
        return rentalCarAddress;
    }

    /**
     * 更新数据
     *
     * @param rentalCarAddress 实例对象
     * @return 实例对象
     */
    public RentalCarAddress update(RentalCarAddress rentalCarAddress) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCarAddress> chainWrapper = new LambdaUpdateChainWrapper<RentalCarAddress>(rentalCarAddressMapper);
        if (StrUtil.isNotBlank(rentalCarAddress.getCarId())) {
            chainWrapper.eq(RentalCarAddress::getCarId, rentalCarAddress.getCarId());
        }
        if (StrUtil.isNotBlank(rentalCarAddress.getUserName())) {
            chainWrapper.eq(RentalCarAddress::getUserName, rentalCarAddress.getUserName());
        }
        if (StrUtil.isNotBlank(rentalCarAddress.getProvince())) {
            chainWrapper.eq(RentalCarAddress::getProvince, rentalCarAddress.getProvince());
        }
        if (StrUtil.isNotBlank(rentalCarAddress.getCity())) {
            chainWrapper.eq(RentalCarAddress::getCity, rentalCarAddress.getCity());
        }
        if (StrUtil.isNotBlank(rentalCarAddress.getCounty())) {
            chainWrapper.eq(RentalCarAddress::getCounty, rentalCarAddress.getCounty());
        }
        if (StrUtil.isNotBlank(rentalCarAddress.getStreet())) {
            chainWrapper.eq(RentalCarAddress::getStreet, rentalCarAddress.getStreet());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalCarAddress::getAddressId, rentalCarAddress.getAddressId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCarAddress.getAddressId());
        } else {
            return rentalCarAddress;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param addressId 主键
     * @return 是否成功
     */
    public boolean deleteById(String addressId) {
        int total = rentalCarAddressMapper.deleteById(addressId);
        return total > 0;
    }
}