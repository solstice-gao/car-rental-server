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

import com.hzwl.rental.entity.user.RentalUserAddress;
import com.hzwl.rental.mapper.user.RentalUserAddressMapper;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Slf4j
@Service
public class RentalUserAddressService {

    @Autowired
    private RentalUserAddressMapper rentalUserAddressMapper;

    @Autowired
    private UserUtils userUtils;

    /**
     * 通过ID查询单条数据
     *
     * @param addressId 主键
     * @return 实例对象
     */
    public RentalUserAddress queryById(String addressId) {
        return rentalUserAddressMapper.selectById(addressId);
    }

    public List<RentalUserAddress> queryAll() {
        String userId = UserUtils.getUserId();
        QueryWrapper<RentalUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return rentalUserAddressMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param rentalUserAddress 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalUserAddress> queryEquities(RentalUserAddress rentalUserAddress, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalUserAddress> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalUserAddress> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalUserAddress.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalUserAddress);
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
        Page<RentalUserAddress> resultPage = rentalUserAddressMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalUserAddress 实例对象
     * @return 实例对象
     */
    public RentalUserAddress insert(RentalUserAddress rentalUserAddress) {
        String userId = UserUtils.getUserId();
        rentalUserAddress.setAddressId(null);
        rentalUserAddress.setUserId(userId);
        rentalUserAddress.setCreateTime(new Date());
        rentalUserAddress.setUpdateTime(new Date());
        rentalUserAddressMapper.insert(rentalUserAddress);
        return rentalUserAddress;
    }

    /**
     * 更新数据
     *
     * @param rentalUserAddress 实例对象
     * @return 实例对象
     */
    public RentalUserAddress update(RentalUserAddress rentalUserAddress) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalUserAddress> chainWrapper = new LambdaUpdateChainWrapper<RentalUserAddress>(rentalUserAddressMapper);
        if (StrUtil.isNotBlank(rentalUserAddress.getUserId())) {
            chainWrapper.eq(RentalUserAddress::getUserId, rentalUserAddress.getUserId());
        }
        if (StrUtil.isNotBlank(rentalUserAddress.getUserName())) {
            chainWrapper.eq(RentalUserAddress::getUserName, rentalUserAddress.getUserName());
        }
        if (StrUtil.isNotBlank(rentalUserAddress.getProvince())) {
            chainWrapper.eq(RentalUserAddress::getProvince, rentalUserAddress.getProvince());
        }
        if (StrUtil.isNotBlank(rentalUserAddress.getCity())) {
            chainWrapper.eq(RentalUserAddress::getCity, rentalUserAddress.getCity());
        }
        if (StrUtil.isNotBlank(rentalUserAddress.getCounty())) {
            chainWrapper.eq(RentalUserAddress::getCounty, rentalUserAddress.getCounty());
        }
        if (StrUtil.isNotBlank(rentalUserAddress.getStreet())) {
            chainWrapper.eq(RentalUserAddress::getStreet, rentalUserAddress.getStreet());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalUserAddress::getAddressId, rentalUserAddress.getAddressId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalUserAddress.getAddressId());
        } else {
            return rentalUserAddress;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param addressId 主键
     * @return 是否成功
     */
    public boolean deleteById(String addressId) {
        int total = rentalUserAddressMapper.deleteById(addressId);
        return total > 0;
    }
}