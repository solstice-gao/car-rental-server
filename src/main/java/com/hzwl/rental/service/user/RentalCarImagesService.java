package com.hzwl.rental.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.constants.ImageStatus;
import com.hzwl.rental.entity.user.RentalCarImages;
import com.hzwl.rental.mapper.user.RentalCarImagesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 车辆图片;(rental_car_images)表服务实现类
 *
 * @author : http://www.chiner.pro
 * @date : 2023-8-30
 */
@Slf4j
@Service
public class RentalCarImagesService {

    @Autowired
    private RentalCarImagesMapper rentalCarImagesMapper;


    /**
     * 根据id获取车辆图片
     * @param carId
     * @return
     */
    public List<String> getImageByCarId(String carId){
        QueryWrapper<RentalCarImages> imageWapper = new QueryWrapper<>();
        imageWapper.eq("car_id", carId);
        return rentalCarImagesMapper.selectList(imageWapper).stream().filter(image -> image.getStatus().equals(ImageStatus.HEADER_IMAGES.getStatus())).map(RentalCarImages::getImageUrl).collect(Collectors.toList());
    }

    /**
     * 根据id获取车辆详情图片
     * @param carId
     * @return
     */
    public List<String> getDescImageByCarId(String carId){
        QueryWrapper<RentalCarImages> imageWapper = new QueryWrapper<>();
        imageWapper.eq("car_id", carId);
        return rentalCarImagesMapper.selectList(imageWapper).stream().filter(image -> image.getStatus().equals(ImageStatus.DESC_IMAGES.getStatus())).map(RentalCarImages::getImageUrl).collect(Collectors.toList());
    }


    /**
     * 通过ID查询单条数据
     *
     * @param imageId 主键
     * @return 实例对象
     */
    public RentalCarImages queryById(String imageId) {
        return rentalCarImagesMapper.selectById(imageId);
    }

    /**
     * 分页查询
     *
     * @param rentalCarImages
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCarImages> queryEquities(RentalCarImages rentalCarImages, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalCarImages> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCarImages> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalCarImages.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalCarImages);
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
        Page<RentalCarImages> resultPage = rentalCarImagesMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalCarImages 实例对象
     * @return 实例对象
     */
    public Boolean insert(RentalCarImages rentalCarImages) {
        int insert = rentalCarImagesMapper.insert(rentalCarImages);
        return insert > 0;
    }

    /**
     * 更新数据
     *
     * @param rentalCarImages 实例对象
     * @return 实例对象
     */
    public RentalCarImages update(RentalCarImages rentalCarImages) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCarImages> chainWrapper = new LambdaUpdateChainWrapper<RentalCarImages>(rentalCarImagesMapper);
        if (StrUtil.isNotBlank(rentalCarImages.getImageUrl())) {
            chainWrapper.eq(RentalCarImages::getImageUrl, rentalCarImages.getImageUrl());
        }
        if (StrUtil.isNotBlank(rentalCarImages.getImageDesc())) {
            chainWrapper.eq(RentalCarImages::getImageDesc, rentalCarImages.getImageDesc());
        }
        if (Objects.nonNull(rentalCarImages.getStatus())) {
            chainWrapper.eq(RentalCarImages::getStatus, rentalCarImages.getStatus());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalCarImages::getImageId, rentalCarImages.getImageId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalCarImages.getImageId());
        } else {
            return rentalCarImages;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param imageId 主键
     * @return 是否成功
     */
    public boolean deleteById(String imageId) {
        int total = rentalCarImagesMapper.deleteById(imageId);
        return total > 0;
    }
}