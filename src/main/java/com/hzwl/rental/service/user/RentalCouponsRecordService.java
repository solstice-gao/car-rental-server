package com.hzwl.rental.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.constants.CouponsStatus;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.ReqRentalCoupons;
import com.hzwl.rental.entity.user.RentalCoupons;
import com.hzwl.rental.entity.user.RentalUser;
import com.hzwl.rental.mapper.user.RentalCouponsMapper;
import com.hzwl.rental.utils.RAssert;
import com.hzwl.rental.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.hzwl.rental.entity.user.RentalCouponsRecord;
import com.hzwl.rental.mapper.user.RentalCouponsRecordMapper;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Slf4j
@Service
public class RentalCouponsRecordService {

    @Autowired
    private RentalCouponsRecordMapper rentalCouponsRecordMapper;

    @Autowired
    private RentalCouponsMapper rentalCouponsMapper;

    @Autowired
    private UserUtils userUtils;


    public synchronized RentalCouponsRecord receive(ReqRentalCoupons coupons) {
        RentalUser userInfo = userUtils.getUserInfo();
        QueryWrapper<RentalCoupons> queryCouponsWrapper = new QueryWrapper<>();
        queryCouponsWrapper.eq("coupon_id", coupons.getCouponId());
        queryCouponsWrapper.eq("active", CouponsStatus.NORMAL.getStatus());
        queryCouponsWrapper.gt("end_date", new Date());
        RentalCoupons rentalCoupons = rentalCouponsMapper.selectOne(queryCouponsWrapper);
        RAssert.nonNull(rentalCoupons, ErrorCode.NOT_FOUND_COUPON);
        QueryWrapper<RentalCouponsRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userInfo.getUserId());
        queryWrapper.eq("coupons_id", rentalCoupons.getCouponId());
        RentalCouponsRecord rentalCouponsRecord = rentalCouponsRecordMapper.selectOne(queryWrapper);
        RAssert.isTrue(Objects.isNull(rentalCouponsRecord), ErrorCode.ALREADY_RECEIVED_COUPON);

        RentalCouponsRecord record = new RentalCouponsRecord();
        record.setUserId(userInfo.getUserId());
        record.setCouponsId(coupons.getCouponId());
        record.setCouponsExpired(rentalCoupons.getEndDate());
        record.setCreateTime(new Date());
        record.setCouponsStatus(CouponsStatus.NORMAL.getStatus());
        rentalCouponsRecordMapper.insert(record);
        return record;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param recordId 主键
     * @return 实例对象
     */
    public RentalCouponsRecord queryById(String recordId) {
        return rentalCouponsRecordMapper.selectById(recordId);
    }

    /**
     * 分页查询
     *
     * @param rentalCouponsRecord 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCouponsRecord> queryEquities(RentalCouponsRecord rentalCouponsRecord, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalCouponsRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCouponsRecord> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalCouponsRecord.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalCouponsRecord);
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
        Page<RentalCouponsRecord> resultPage = rentalCouponsRecordMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalCouponsRecord 实例对象
     * @return 实例对象
     */
    public RentalCouponsRecord insert(RentalCouponsRecord rentalCouponsRecord) {
        rentalCouponsRecordMapper.insert(rentalCouponsRecord);
        return rentalCouponsRecord;
    }

    /**
     * 更新数据
     *
     * @param rentalCouponsRecord 实例对象
     * @return 实例对象
     */
    public RentalCouponsRecord update(RentalCouponsRecord rentalCouponsRecord) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCouponsRecord> chainWrapper = new LambdaUpdateChainWrapper<RentalCouponsRecord>(rentalCouponsRecordMapper);
        if (StrUtil.isNotBlank(rentalCouponsRecord.getCouponsId())) {
            chainWrapper.set(RentalCouponsRecord::getCouponsId, rentalCouponsRecord.getCouponsId());
        }
        if (StrUtil.isNotBlank(rentalCouponsRecord.getUserId())) {
            chainWrapper.set(RentalCouponsRecord::getUserId, rentalCouponsRecord.getUserId());
        }
            if (Objects.nonNull(rentalCouponsRecord.getCouponsStatus())) {
            chainWrapper.set(RentalCouponsRecord::getCouponsStatus, rentalCouponsRecord.getCouponsStatus());
        }
        //2. 设置主键，并更新
        chainWrapper.eq(RentalCouponsRecord::getRecordId, rentalCouponsRecord.getRecordId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCouponsRecord.getRecordId());
        } else {
            return rentalCouponsRecord;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param recordId 主键
     * @return 是否成功
     */
    public boolean deleteById(String recordId) {
        int total = rentalCouponsRecordMapper.deleteById(recordId);
        return total > 0;
    }

    public List<RentalCouponsRecord> queryNormalByUserId(String userId) {
        QueryWrapper<RentalCouponsRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("coupons_status", CouponsStatus.NORMAL.getStatus());
        queryWrapper.gt("coupons_expired", new Date());
        return rentalCouponsRecordMapper.selectList(queryWrapper);
    }
    public List<RentalCouponsRecord> queryAllByUserId(String userId) {
        QueryWrapper<RentalCouponsRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return rentalCouponsRecordMapper.selectList(queryWrapper);
    }

}