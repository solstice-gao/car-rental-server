package com.hzwl.rental.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hzwl.rental.constants.CarStatus;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.ResGroupByCompany;
import com.hzwl.rental.entity.dto.ResRentalCars;
import com.hzwl.rental.entity.user.*;
import com.hzwl.rental.mapper.user.RentalCarsMapper;
import com.hzwl.rental.utils.RAssert;
import com.hzwl.rental.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 车辆信息;(rental_cars)表服务实现类
 *
 * @author : http://www.chiner.pro
 * @date : 2023-8-30
 */
@Slf4j
@Service
public class RentalCarsService {

    @Autowired
    private RentalCarsMapper rentalCarsMapper;

    @Autowired
    private RentalCarImagesService rentalCarImagesService;


    @Autowired
    private RentalCompanyService rentalCompanyService;

    @Autowired
    private RentalCarModelsService rentalCarModelsService;


    @Autowired
    private RentalDeliveryAddressService rentalDeliveryAddressService;

    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    public RentalCars queryById(String carId) {
        return rentalCarsMapper.selectById(carId);
    }


    /**
     * 下架
     * @param carId
     * @return
     */
    public Boolean offTheShelf(String carId, Integer status) {
        RentalCars rentalCars = rentalCarsMapper.selectById(carId);
        RAssert.nonNull(rentalCars, ErrorCode.NOT_FOUND_CAR_INFO);
        if(status.equals(CarStatus.NORMAL.getStatus())){
            rentalCars.setStatus(CarStatus.WAITINGRELEASE.getStatus());
        }else if (status.equals(CarStatus.WAITINGRELEASE.getStatus())){
            rentalCars.setStatus(CarStatus.NORMAL.getStatus());
        }
        return rentalCarsMapper.updateById(rentalCars) > 0;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param carId 主键
     * @return 实例对象
     */
    public ResRentalCars queryResById(String carId) {
        RentalCars rentalCars = rentalCarsMapper.selectById(carId);
        List<RentalCarModels> rentalCarModels = rentalCarModelsService.queryByCarId(carId);
        ResRentalCars resRentalCars = new ResRentalCars();

        String companyId = rentalCars.getCompanyId();
        RentalCompany rentalCompany = rentalCompanyService.queryById(companyId);
        resRentalCars.setCompanyInfo(rentalCompany);
        BeanUtil.copyProperties(rentalCars, resRentalCars);

        DoubleSummaryStatistics priceStats = rentalCarModels.stream()
                .mapToDouble(item -> {
                    return item.getCarPrice().doubleValue();
                })
                .summaryStatistics();

        resRentalCars.setRangePrice((int) priceStats.getMin() + "-" + (int) priceStats.getMax());

        QueryWrapper<RentalCarImages> imageWapper = new QueryWrapper<>();
        imageWapper.eq("car_id", rentalCars.getCarId());
        ExecutorService executorService = threadPoolUtils.getExecutorService();
        CompletableFuture<List<String>> rentalCarIamgeFuture = CompletableFuture.supplyAsync(() -> rentalCarImagesService.getImageByCarId(rentalCars.getCarId()), executorService);
        CompletableFuture<List<String>> rentalCarsDescImageFuture = CompletableFuture.supplyAsync(() -> rentalCarImagesService.getDescImageByCarId(rentalCars.getCarId()), executorService);
        CompletableFuture<List<RentalDeliveryAddress>> rentalDeliveryAddressFuture = CompletableFuture.supplyAsync(() -> rentalDeliveryAddressService.queryByCarId(rentalCars.getCarId()), executorService);
        CompletableFuture.allOf(rentalCarIamgeFuture, rentalCarsDescImageFuture, rentalDeliveryAddressFuture).join();

        List<String> rentalCarImages = rentalCarImagesService.getImageByCarId(rentalCars.getCarId());
        List<String> rentalCarDescImages = rentalCarImagesService.getDescImageByCarId(rentalCars.getCarId());
        List<RentalDeliveryAddress> rentalDeliveryAddresses = rentalDeliveryAddressService.queryByCarId(rentalCars.getCarId());

        resRentalCars.setDeliveryAddresses(rentalDeliveryAddresses);
        resRentalCars.setCarImages(rentalCarImages);
        resRentalCars.setCarDescImages(rentalCarDescImages);
        resRentalCars.setCarModels(rentalCarModels);

        return resRentalCars;
    }

    /**
     * 分页查询
     *
     * @param rentalCars
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCars> queryEquities(RentalCars rentalCars, Integer pageNum, Integer pageSize, String startTime, String endTime) {
        Page<RentalCars> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCars> queryWrapper = new QueryWrapper<>();
        Page<RentalCars> resultPage = rentalCarsMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * @return
     */
    public List<ResGroupByCompany> queryGroupByCompany(String status) throws ExecutionException, InterruptedException {
        QueryWrapper<RentalCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        List<ResGroupByCompany> result = Lists.newArrayList();
        ExecutorService executorService = threadPoolUtils.getExecutorService();
        CompletableFuture<List<RentalCars>> rentalCarsFuture = CompletableFuture.supplyAsync(() -> rentalCarsMapper.selectList(queryWrapper), executorService);
        CompletableFuture<List<RentalCompany>> rentalCompanyFuture = CompletableFuture.supplyAsync(() -> rentalCompanyService.queryAllByAdmin(), executorService);
        CompletableFuture<List<RentalCarModels>> rentalCarModelsFuture = CompletableFuture.supplyAsync(() -> rentalCarModelsService.queryAll(), executorService);
        CompletableFuture.allOf(rentalCarsFuture, rentalCompanyFuture, rentalCarModelsFuture).join();

        List<RentalCompany> rentalCompanies = rentalCompanyFuture.get();
        List<RentalCars> rentalCars = rentalCarsFuture.get();
        List<RentalCarModels> rentalCarModels = rentalCarModelsFuture.get();
        try {
            rentalCompanies.forEach(rentalCompany -> {
                ResGroupByCompany resGroupByCompany = new ResGroupByCompany();
                BeanUtil.copyProperties(rentalCompany, resGroupByCompany);
                List<ResRentalCars> collect = rentalCars.stream().filter(car -> car.getCompanyId().equals(rentalCompany.getCompanyId())).map(car -> {
                    ResRentalCars resRentalCars = new ResRentalCars();
                    BeanUtil.copyProperties(car, resRentalCars);
                    List<RentalCarModels> models = rentalCarModels.stream().filter(model -> model.getCarId().equals(car.getCarId())).collect(Collectors.toList());
                    resRentalCars.setCarModels(models);
                    return resRentalCars;

                }).collect(Collectors.toList());
                resGroupByCompany.setCarList(collect);
                result.add(resGroupByCompany);
            });
        }catch (Exception e){
            System.out.println(e);
        }
        return result;
    }


    /**
     * 新增数据
     *
     * @param rentalCars 实例对象
     * @return 实例对象
     */
    public RentalCars insert(RentalCars rentalCars) {
        rentalCarsMapper.insert(rentalCars);
        return rentalCars;
    }

    /**
     * 更新数据
     *
     * @param rentalCars 实例对象
     * @return 实例对象
     */
    public boolean update(RentalCars rentalCars) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCars> chainWrapper = new LambdaUpdateChainWrapper<RentalCars>(rentalCarsMapper);
        if (StrUtil.isNotBlank(rentalCars.getCarDesc())) {
            chainWrapper.set(RentalCars::getCarDesc, rentalCars.getCarDesc());
        }
        if (StrUtil.isNotBlank(rentalCars.getModelId())) {
            chainWrapper.set(RentalCars::getModelId, rentalCars.getModelId());
        }
        if (StrUtil.isNotBlank(rentalCars.getStoreId())) {
            chainWrapper.set(RentalCars::getStoreId, rentalCars.getStoreId());
        }
        if (Objects.nonNull(rentalCars.getDiscount())) {
            chainWrapper.set(RentalCars::getDiscount, rentalCars.getDiscount());
        }
        if (StrUtil.isNotBlank(rentalCars.getLicensePlate())) {
            chainWrapper.set(RentalCars::getLicensePlate, rentalCars.getLicensePlate());
        }
        if (Objects.nonNull(rentalCars.getStatus())) {
            chainWrapper.set(RentalCars::getStatus, rentalCars.getStatus());
        }
        if (StrUtil.isNotBlank(rentalCars.getImageUrl())) {
            chainWrapper.set(RentalCars::getImageUrl, rentalCars.getImageUrl());
        }
        //2. 设置主键，并更新
        chainWrapper.eq(RentalCars::getCarId, rentalCars.getCarId());
        return chainWrapper.update();
    }

    /**
     * 通过主键删除数据
     *
     * @param carId 主键
     * @return 是否成功
     */
    public boolean deleteById(String carId) {
        int total = rentalCarsMapper.deleteById(carId);
        return total > 0;
    }

    public boolean putShelves(String carId) {
        RentalCars rentalCars = this.queryById(carId);
        RAssert.nonNull(rentalCars, ErrorCode.NOT_FOUND_CAR_INFO);
        rentalCars.setStatus(CarStatus.NORMAL.getStatus());
        return this.update(rentalCars);
    }
}