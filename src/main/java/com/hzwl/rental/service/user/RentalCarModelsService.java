package com.hzwl.rental.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.CarStatus;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.ReqColorModel;
import com.hzwl.rental.entity.dto.ResRentalCars;
import com.hzwl.rental.entity.user.RentalCarAddress;
import com.hzwl.rental.entity.user.RentalCarModels;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.mapper.user.RentalCarModelsMapper;
import com.hzwl.rental.mapper.user.RentalCarsMapper;
import com.hzwl.rental.utils.RAssert;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class RentalCarModelsService {

    @Autowired
    private RentalCarModelsMapper rentalCarModelsMapper;


    @Autowired
    private RentalCarAddressService rentalCarAddressService;

    @Autowired
    private RentalCarsMapper rentalCarsMapper;

    public List<RentalCarModels> queryAll(){
        QueryWrapper<RentalCarModels> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("model_id", 0);
        return rentalCarModelsMapper.selectList(queryWrapper);
    }

    /**
     * 新增车辆以及型号信息
     *
     * @param reqColorModel
     * @return
     */
    @Transactional
    public RentalCars addColorModel(ReqColorModel reqColorModel) {
        RentalCars rentalCars = new RentalCars();
        rentalCars.setStatus(CarStatus.WAITINGRELEASE.getStatus());
        rentalCars.setUpdatedTime(new Date());
        rentalCars.setCreatedTime(new Date());
        rentalCars.setCompanyId(reqColorModel.getCompanyId());
        rentalCars.setCarDesc(reqColorModel.getCarDesc());
        rentalCars.setCarDescTitle(reqColorModel.getCarDescTitle());
        rentalCars.setPrice(reqColorModel.getPrice());
        rentalCars.setCarSendFrom(reqColorModel.getCarSendFrom());
        rentalCars.setCarSendMarktext(reqColorModel.getCarSendMarktext());
        int insert = rentalCarsMapper.insert(rentalCars);

        if (insert <= 0) {
            throw new BizException(ErrorCode.INTERNAL_CALL_ERROR);
        }


        List<String> colors = reqColorModel.getColors();
        List<String> models = reqColorModel.getModels();
        List<RentalCarModels> prices = reqColorModel.getPrices();
        RAssert.isTrue(models.size() == prices.size(), ErrorCode.CAR_PRICE_MODELS_NOT_EQUALS);

        List<RentalCarModels> insertDatas = Lists.newArrayList();

        colors.forEach(color -> {
            for (int i = 0; i < models.size(); i++) {
                RentalCarModels rentalCarModels = new RentalCarModels();
                RentalCarModels source = prices.get(i);
                BeanUtil.copyProperties(source, rentalCarModels);
                rentalCarModels.setCarId(rentalCars.getCarId());
                rentalCarModels.setColor(color);
                rentalCarModels.setModel(models.get(i));
                rentalCarModels.setCreatedTime(new Date());
                rentalCarModels.setUpdatedTime(new Date());
                insertDatas.add(rentalCarModels);
            }
        });

        for (RentalCarModels insertData : insertDatas) {
            this.insert(insertData);
        }
        List<RentalCarAddress> adressList = reqColorModel.getAdress();
        adressList.forEach(adress->{
            adress.setCarId(rentalCars.getCarId());
            adress.setCreateTime(new Date());
            adress.setUpdateTime(new Date());
            rentalCarAddressService.insert(adress);
        });

        return rentalCars;
    }


    public List<RentalCarModels> queryByCarId(String carId) {
        QueryWrapper<RentalCarModels> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", carId);
        return rentalCarModelsMapper.selectList(queryWrapper);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param modelId 主键
     * @return 实例对象
     */
    public RentalCarModels queryById(String modelId) {
        return rentalCarModelsMapper.selectById(modelId);
    }

    /**
     * 分页查询
     *
     * @param rentalCarModels
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCarModels> queryEquities(RentalCarModels rentalCarModels, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalCarModels> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCarModels> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalCarModels.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalCarModels);
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
        Page<RentalCarModels> resultPage = rentalCarModelsMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalCarModels 实例对象
     * @return 实例对象
     */
    public boolean insert(RentalCarModels rentalCarModels) {
        int insert = rentalCarModelsMapper.insert(rentalCarModels);
        return insert > 0;
    }

    /**
     * 更新数据
     *
     * @param rentalCarModels 实例对象
     * @return 实例对象
     */
    public RentalCarModels update(RentalCarModels rentalCarModels) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCarModels> chainWrapper = new LambdaUpdateChainWrapper<RentalCarModels>(rentalCarModelsMapper);
        if (StrUtil.isNotBlank(rentalCarModels.getCarId())) {
            chainWrapper.eq(RentalCarModels::getCarId, rentalCarModels.getCarId());
        }
        if (StrUtil.isNotBlank(rentalCarModels.getBrand())) {
            chainWrapper.eq(RentalCarModels::getBrand, rentalCarModels.getBrand());
        }
        if (StrUtil.isNotBlank(rentalCarModels.getColor())) {
            chainWrapper.eq(RentalCarModels::getColor, rentalCarModels.getColor());
        }
        if (StrUtil.isNotBlank(rentalCarModels.getModel())) {
            chainWrapper.eq(RentalCarModels::getModel, rentalCarModels.getModel());
        }
        if (StrUtil.isNotBlank(rentalCarModels.getDescription())) {
            chainWrapper.eq(RentalCarModels::getDescription, rentalCarModels.getDescription());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalCarModels::getModelId, rentalCarModels.getModelId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCarModels.getModelId());
        } else {
            return rentalCarModels;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param modelId 主键
     * @return 是否成功
     */
    public boolean deleteById(String modelId) {
        int total = rentalCarModelsMapper.deleteById(modelId);
        return total > 0;
    }


}