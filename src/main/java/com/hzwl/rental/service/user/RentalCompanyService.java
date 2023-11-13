package com.hzwl.rental.service.user;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hzwl.rental.constants.CarStatus;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.Location;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.entity.user.RentalCompany;
import com.hzwl.rental.entity.dto.ResRentalCompanyCars;
import com.hzwl.rental.mapper.user.RentalCarsMapper;
import com.hzwl.rental.mapper.user.RentalCompanyMapper;
import com.hzwl.rental.utils.RAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 公司名称;(rental_company)表服务实现类
 *
 * @author : http://www.chiner.pro
 * @date : 2023-8-30
 */
@Slf4j
@Service
public class RentalCompanyService {

    @Autowired
    private RentalCompanyMapper rentalCompanyMapper;

    @Autowired
    private RentalCarsMapper carsMapper;

    @Resource
    private IPService ipService;

    /**
     * 通过ID查询单条数据
     *
     * @param companyId 主键
     * @return 实例对象
     */
    public RentalCompany queryById(String companyId) {
        return rentalCompanyMapper.selectById(companyId);
    }

    /**
     * 分页查询
     *
     * @param rentalCompany
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCompany> queryEquities(RentalCompany rentalCompany, Integer pageNum, Integer pageSize, String startTime, String endTime, String companyName) {

        Page<RentalCompany> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCompany> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.between("create_time", startTime, endTime);
        }
        if(StringUtils.isNotBlank(companyName)){
            queryWrapper.like("company_name",companyName);
        }


        Page<RentalCompany> resultPage = rentalCompanyMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalCompany 实例对象
     * @return 实例对象
     */
    public RentalCompany insert(RentalCompany rentalCompany) {
        rentalCompanyMapper.insert(rentalCompany);
        return rentalCompany;
    }

    /**
     * 更新数据
     *
     * @param rentalCompany 实例对象
     * @return 实例对象
     */
    public RentalCompany update(RentalCompany rentalCompany) {
        QueryWrapper<RentalCompany> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(rentalCompany.getCompanyName())){
            queryWrapper.eq("company_name",rentalCompany.getCompanyName());
        }
        RentalCompany company = rentalCompanyMapper.selectOne(queryWrapper);
        if (company != null){
            RAssert.isTrue(company.getCompanyId().equals(rentalCompany.getCompanyId()), ErrorCode.COMPANY_NAME_EXITES);
        }


        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCompany> chainWrapper = new LambdaUpdateChainWrapper<RentalCompany>(rentalCompanyMapper);
        if (StrUtil.isNotBlank(rentalCompany.getAddressId())) {
            chainWrapper.set(RentalCompany::getAddressId, rentalCompany.getAddressId());
        }
        if (StrUtil.isNotBlank(rentalCompany.getCompanyName())) {
            chainWrapper.set(RentalCompany::getCompanyName, rentalCompany.getCompanyName());
        }
        if (StrUtil.isNotBlank(rentalCompany.getCompanyMonthlySales())) {
            chainWrapper.set(RentalCompany::getCompanyMonthlySales, rentalCompany.getCompanyMonthlySales());
        }
        if (StrUtil.isNotBlank(rentalCompany.getCompanyIcon())) {
            chainWrapper.set(RentalCompany::getCompanyIcon, rentalCompany.getCompanyIcon());
        }



        //2. 设置主键，并更新
        chainWrapper.eq(RentalCompany::getCompanyId, rentalCompany.getCompanyId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCompany.getCompanyId());
        } else {
            return rentalCompany;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param companyId 主键
     * @return 是否成功
     */
    public boolean deleteById(String companyId) {
        int total = rentalCompanyMapper.deleteById(companyId);
        return total > 0;
    }

    /**
     * 根据IP获取公司所有车辆信息
     *
     * @param ip
     * @return
     */
    public List<ResRentalCompanyCars> queryCompanyCars(String ip) {

        QueryWrapper<RentalCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("company_id", 0);

        List<ResRentalCompanyCars> result = Lists.newArrayList();
        List<RentalCompany> rentalCompanies = rentalCompanyMapper.selectList(queryWrapper);

        try {
            Location location = ipService.getLocation(ip);
            if (Objects.nonNull(location)) {
                String city = location.getCity();
                rentalCompanies = rentalCompanies.stream()
                        .filter(company -> city.contains(company.getCompanyCity()))
                        .collect(Collectors.toList());
            } else {
                if (rentalCompanies.size() > 5) {
                    rentalCompanies = rentalCompanies.subList(0, 5);
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        rentalCompanies.stream().forEach(rentalCompany -> {
            QueryWrapper<RentalCars> carWrapper = new QueryWrapper<>();
            carWrapper.eq("company_id", rentalCompany.getCompanyId());
            // 过滤状态为1的数据
            List<RentalCars> rentalCars = carsMapper.selectList(carWrapper).stream().filter(item -> item.getStatus() == CarStatus.NORMAL.getStatus()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(rentalCars)) {
                ResRentalCompanyCars cars = new ResRentalCompanyCars();
                cars.setCompanyId(rentalCompany.getCompanyId());
                cars.setCompanyIcon(rentalCompany.getCompanyIcon());
                cars.setCompanyName(rentalCompany.getCompanyName());
                cars.setCarList(rentalCars);
                result.add(cars);
            }
        });
        return result;
    }

    public List<ResRentalCompanyCars> queryByCompanyId(String companyId) {
        RentalCompany rentalCompany = rentalCompanyMapper.selectById(companyId);
        List<ResRentalCompanyCars> result = Lists.newArrayList();
        QueryWrapper<RentalCars> carWrapper = new QueryWrapper<>();
        carWrapper.eq("company_id", companyId);
        // 过滤状态为1的数据
        List<RentalCars> rentalCars = carsMapper.selectList(carWrapper).stream().filter(item -> item.getStatus() == CarStatus.NORMAL.getStatus()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(rentalCars)) {
            ResRentalCompanyCars cars = new ResRentalCompanyCars();
            cars.setCompanyId(rentalCompany.getCompanyId());
            cars.setCompanyIcon(rentalCompany.getCompanyIcon());
            cars.setCompanyName(rentalCompany.getCompanyName());
            cars.setCarList(rentalCars);
            result.add(cars);
        }
        return result;
    }

    public List<RentalCompany> queryAllByAdmin() {
        QueryWrapper<RentalCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("company_id", 0);

        List<ResRentalCompanyCars> result = Lists.newArrayList();
        List<RentalCompany> rentalCompanies = rentalCompanyMapper.selectList(queryWrapper);
        return rentalCompanies;
    }
}