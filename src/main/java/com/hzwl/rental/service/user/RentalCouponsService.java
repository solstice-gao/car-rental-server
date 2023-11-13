package com.hzwl.rental.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.entity.user.*;
import com.hzwl.rental.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hzwl.rental.mapper.user.RentalCouponsMapper;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Slf4j
@Service
public class RentalCouponsService {

    @Autowired
    private RentalCouponsMapper rentalCouponsMapper;

    @Autowired
    private RentalCouponsRecordService rentalCouponsRecordService;
    
    @Autowired
    private RentalCompanyService rentalCompanyService;


    @Autowired
    private RentalCouponsCompanyService rentalCouponsCompanyService;


    @Autowired
    private UserUtils userUtils;


    public List<RentalCoupons> queryAll() {
        QueryWrapper<RentalCoupons> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("active", 1);
        queryWrapper.gt("end_date", new Date());
        RentalUser userInfo = userUtils.getUserInfo();
        List<RentalCoupons> rentalCoupons = rentalCouponsMapper.selectList(queryWrapper);
        List<String> records = rentalCouponsRecordService.queryAllByUserId(userInfo.getUserId()).stream().map(RentalCouponsRecord::getCouponsId).collect(Collectors.toList());
        List<RentalCoupons> collect = rentalCoupons.stream()
                .filter(coupons -> coupons.getMaxUsage() > coupons.getUsedCount())
                .filter(coupons -> !records.contains(coupons.getCouponId()))
                .collect(Collectors.toList());
        return collect;
    }


    /**
     * 通过ID查询单条数据
     *
     * @param couponId 主键
     * @return 实例对象
     */
    public RentalCoupons queryById(String couponId) {
        return rentalCouponsMapper.selectById(couponId);
    }

    /**
     * 分页查询
     *
     * @param rentalCoupons 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCoupons> queryEquities(RentalCoupons rentalCoupons, Integer pageNum, Integer pageSize, String startTime, String endTime) {
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        Page<RentalCoupons> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCoupons> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (rentalCoupons != null) {
            if (rentalCoupons.getStartDate() != null) {
                queryWrapper.ge("start_date", rentalCoupons.getStartDate());
            }
            if (rentalCoupons.getEndDate() != null) {
                queryWrapper.le("end_date", rentalCoupons.getEndDate());
            }
            if (StringUtils.isNotBlank(rentalCoupons.getTitle())) {
                queryWrapper.and(wrapper -> wrapper
                        .like("title", rentalCoupons.getTitle())
                        .or()
                        .like("description", rentalCoupons.getTitle())
                        .or()
                        .like("coupon_id", rentalCoupons.getTitle())
                );
            }
        }

        return rentalCouponsMapper.selectPage(page, queryWrapper);
    }



    /**
     * 新增数据
     *
     * @param rentalCoupons 实例对象
     * @return 实例对象
     */
    public RentalCoupons insert(RentalCoupons rentalCoupons) {
        rentalCoupons.setCreatedAt(new Date());
        rentalCoupons.setUpdatedAt(new Date());
        rentalCoupons.setDiscountType("指定店铺可用");
        rentalCouponsMapper.insert(rentalCoupons);

        List<RentalCompany> rentalCompanies = rentalCompanyService.queryAllByAdmin();
        rentalCompanies.forEach(rentalCompany -> {
            RentalCouponsCompany rentalCouponsCompany = new RentalCouponsCompany();
            rentalCouponsCompany.setCouponsId(rentalCoupons.getCouponId());
            rentalCouponsCompany.setCompanyId(rentalCompany.getCompanyId());
            rentalCouponsCompany.setCreatedTime(new Date());

            rentalCouponsCompanyService.insert(rentalCouponsCompany);
        });
        return rentalCoupons;
    }

    /**
     * 更新数据
     *
     * @param rentalCoupons 实例对象
     * @return 实例对象
     */
    public RentalCoupons update(RentalCoupons rentalCoupons) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCoupons> chainWrapper = new LambdaUpdateChainWrapper<RentalCoupons>(rentalCouponsMapper);
        if (StrUtil.isNotBlank(rentalCoupons.getCouponCode())) {
            chainWrapper.eq(RentalCoupons::getCouponCode, rentalCoupons.getCouponCode());
        }
        if (StrUtil.isNotBlank(rentalCoupons.getTitle())) {
            chainWrapper.eq(RentalCoupons::getTitle, rentalCoupons.getTitle());
        }
        if (StrUtil.isNotBlank(rentalCoupons.getDescription())) {
            chainWrapper.eq(RentalCoupons::getDescription, rentalCoupons.getDescription());
        }
        if (StrUtil.isNotBlank(rentalCoupons.getActive())) {
            chainWrapper.eq(RentalCoupons::getActive, rentalCoupons.getActive());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalCoupons::getCouponId, rentalCoupons.getCouponId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCoupons.getCouponId());
        } else {
            return rentalCoupons;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param couponId 主键
     * @return 是否成功
     */
    public boolean deleteById(String couponId) {
        int total = rentalCouponsMapper.deleteById(couponId);
        return total > 0;
    }
}