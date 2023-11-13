package com.hzwl.rental.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.entity.user.RentalCarImages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

import com.hzwl.rental.entity.user.RentalDeliveryAddress;
import com.hzwl.rental.mapper.user.RentalDeliveryAddressMapper;
import com.hzwl.rental.service.user.RentalDeliveryAddressService;

/**
 * @Author GA666666
 * @Date 2023/9/14 12:43
 */
@Slf4j
@Service
public class RentalDeliveryAddressService {

    @Autowired
    private RentalDeliveryAddressMapper rentalDeliveryAddressMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param deliveryId 主键
     * @return 实例对象
     */
    public RentalDeliveryAddress queryById(String deliveryId) {
        return rentalDeliveryAddressMapper.selectById(deliveryId);
    }


    /**
     * 通过ID查询单条数据
     *
     * @param carId 主键
     * @return 实例对象
     */
    public List<RentalDeliveryAddress> queryByCarId(String carId) {
        QueryWrapper<RentalDeliveryAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", carId);
        return rentalDeliveryAddressMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param rentalDeliveryAddress 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalDeliveryAddress> queryEquities(RentalDeliveryAddress rentalDeliveryAddress, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalDeliveryAddress> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalDeliveryAddress> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalDeliveryAddress.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalDeliveryAddress);
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
        Page<RentalDeliveryAddress> resultPage = rentalDeliveryAddressMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalDeliveryAddress 实例对象
     * @return 实例对象
     */
    public RentalDeliveryAddress insert(RentalDeliveryAddress rentalDeliveryAddress) {
        rentalDeliveryAddressMapper.insert(rentalDeliveryAddress);
        return rentalDeliveryAddress;
    }

    /**
     * 更新数据
     *
     * @param rentalDeliveryAddress 实例对象
     * @return 实例对象
     */
    public RentalDeliveryAddress update(RentalDeliveryAddress rentalDeliveryAddress) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalDeliveryAddress> chainWrapper = new LambdaUpdateChainWrapper<RentalDeliveryAddress>(rentalDeliveryAddressMapper);
        if (StrUtil.isNotBlank(rentalDeliveryAddress.getCarId())) {
            chainWrapper.eq(RentalDeliveryAddress::getCarId, rentalDeliveryAddress.getCarId());
        }
        if (StrUtil.isNotBlank(rentalDeliveryAddress.getAddressName())) {
            chainWrapper.eq(RentalDeliveryAddress::getAddressName, rentalDeliveryAddress.getAddressName());
        }
        if (StrUtil.isNotBlank(rentalDeliveryAddress.getProvince())) {
            chainWrapper.eq(RentalDeliveryAddress::getProvince, rentalDeliveryAddress.getProvince());
        }
        if (StrUtil.isNotBlank(rentalDeliveryAddress.getCity())) {
            chainWrapper.eq(RentalDeliveryAddress::getCity, rentalDeliveryAddress.getCity());
        }
        if (StrUtil.isNotBlank(rentalDeliveryAddress.getCounty())) {
            chainWrapper.eq(RentalDeliveryAddress::getCounty, rentalDeliveryAddress.getCounty());
        }
        if (StrUtil.isNotBlank(rentalDeliveryAddress.getStreet())) {
            chainWrapper.eq(RentalDeliveryAddress::getStreet, rentalDeliveryAddress.getStreet());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalDeliveryAddress::getDeliveryId, rentalDeliveryAddress.getDeliveryId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalDeliveryAddress.getDeliveryId());
        } else {
            return rentalDeliveryAddress;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param deliveryId 主键
     * @return 是否成功
     */
    public boolean deleteById(String deliveryId) {
        int total = rentalDeliveryAddressMapper.deleteById(deliveryId);
        return total > 0;
    }
}