package com.hzwl.rental.service.user;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.entity.dto.ReqRentalCouponsCompany;
import com.hzwl.rental.entity.user.*;
import com.hzwl.rental.mapper.user.RentalCouponsMapper;
import com.hzwl.rental.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import com.hzwl.rental.mapper.user.RentalCouponsCompanyMapper;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Slf4j
@Service
public class RentalCouponsCompanyService {

    @Autowired
    private RentalCouponsCompanyMapper rentalCouponsCompanyMapper;


    @Autowired
    private RentalCarModelsService rentalCarModelsService;

    @Autowired
    private RentalCouponsRecordService rentalCouponsRecordService;

    @Autowired
    private RentalCouponsMapper rentalCouponsMapper;


    @Autowired
    private UserUtils userUtils;


    public List<RentalCoupons> queryByCompany(ReqRentalCouponsCompany rentalCouponsCompany) {
        RentalUser userInfo = userUtils.getUserInfo();
        List<RentalCouponsRecord> records = rentalCouponsRecordService.queryNormalByUserId(userInfo.getUserId());
        RentalCarModels rentalCarModels = rentalCarModelsService.queryById(rentalCouponsCompany.getModelId());
        QueryWrapper<RentalCouponsCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", rentalCouponsCompany.getCompanyId());
        List<String> rentalCouponsCompanies = rentalCouponsCompanyMapper.selectList(queryWrapper).stream().map(RentalCouponsCompany::getCouponsId).collect(Collectors.toList());
        List<String> collect = records.stream().map(RentalCouponsRecord::getCouponsId).filter(rentalCouponsCompanies::contains).collect(Collectors.toList());

        if(collect.size() == 0){
            return ListUtil.empty();
        }

        List<RentalCoupons> rentalCoupons = rentalCouponsMapper.selectBatchIds(collect);
        return rentalCoupons;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param companyCouponsId 主键
     * @return 实例对象
     */
    public RentalCouponsCompany queryById(String companyCouponsId) {
        return rentalCouponsCompanyMapper.selectById(companyCouponsId);
    }

    /**
     * 分页查询
     *
     * @param rentalCouponsCompany 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCouponsCompany> queryEquities(RentalCouponsCompany rentalCouponsCompany, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalCouponsCompany> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCouponsCompany> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalCouponsCompany.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalCouponsCompany);
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
        Page<RentalCouponsCompany> resultPage = rentalCouponsCompanyMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalCouponsCompany 实例对象
     * @return 实例对象
     */
    public RentalCouponsCompany insert(RentalCouponsCompany rentalCouponsCompany) {
        rentalCouponsCompanyMapper.insert(rentalCouponsCompany);
        return rentalCouponsCompany;
    }

    /**
     * 更新数据
     *
     * @param rentalCouponsCompany 实例对象
     * @return 实例对象
     */
    public RentalCouponsCompany update(RentalCouponsCompany rentalCouponsCompany) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCouponsCompany> chainWrapper = new LambdaUpdateChainWrapper<RentalCouponsCompany>(rentalCouponsCompanyMapper);
        if (StrUtil.isNotBlank(rentalCouponsCompany.getCouponsId())) {
            chainWrapper.eq(RentalCouponsCompany::getCouponsId, rentalCouponsCompany.getCouponsId());
        }
        if (StrUtil.isNotBlank(rentalCouponsCompany.getCompanyId())) {
            chainWrapper.eq(RentalCouponsCompany::getCompanyId, rentalCouponsCompany.getCompanyId());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalCouponsCompany::getCompanyCouponsId, rentalCouponsCompany.getCompanyCouponsId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCouponsCompany.getCompanyCouponsId());
        } else {
            return rentalCouponsCompany;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param companyCouponsId 主键
     * @return 是否成功
     */
    public boolean deleteById(String companyCouponsId) {
        int total = rentalCouponsCompanyMapper.deleteById(companyCouponsId);
        return total > 0;
    }
}
