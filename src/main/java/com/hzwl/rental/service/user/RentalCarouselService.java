package com.hzwl.rental.service.user;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hzwl.rental.constants.CarouselStatus;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.Location;
import com.hzwl.rental.entity.dto.ResRentalCarousel;
import com.hzwl.rental.entity.dto.ResRentalCompanyCars;
import com.hzwl.rental.entity.tenant.RentalTenantUser;
import com.hzwl.rental.entity.user.RentalCarImages;
import com.hzwl.rental.entity.user.RentalCarousel;
import com.hzwl.rental.entity.user.RentalCompany;
import com.hzwl.rental.mapper.user.RentalCarouselMapper;
import com.hzwl.rental.service.tenant.FileUploadService;
import com.hzwl.rental.service.tenant.RentalTenantUserService;
import com.hzwl.rental.utils.RAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Author GA666666
 * @Date  2023/10/10 12:43
 */
@Slf4j
@Service
public class RentalCarouselService{

    @Autowired
    private RentalCarouselMapper rentalCarouselMapper;

    @Autowired
    private RentalTenantUserService rentalTenantUserService;

    @Autowired
    private FileUploadService fileUploadService;

    @Resource
    private IPService ipService;
    
    /** 
     * 通过ID查询单条数据 
     *
     * @param carouselId 主键
     * @return 实例对象
     */
    public RentalCarousel queryById(String carouselId){
        return rentalCarouselMapper.selectById(carouselId);
    }
    
    /**
     * 分页查询
     *
     * @param rentalCarousel 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalCarousel> queryEquities(RentalCarousel rentalCarousel, Integer pageNum, Integer pageSize, String startTime, String endTime){
        
        Page<RentalCarousel> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalCarousel> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalCarousel.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalCarousel);
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
        Page<RentalCarousel> resultPage = rentalCarouselMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     * @param file
     * @param city
     * @return
     * @throws IOException
     */
    public RentalCarousel insert(MultipartFile file, String city) throws IOException {

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = (String) tokenInfo.getLoginId();
        Assert.isTrue(tokenInfo.getIsLogin(), ErrorCode.TOKEN_EXPIRATION.getErrorMsg());

        RentalTenantUser tenantUser = rentalTenantUserService.queryById(tokenValue);
        RAssert.nonNull(tenantUser, ErrorCode.TENNAT_IS_BLANK);
        String url = fileUploadService.uploadFile(file);

        RentalCarousel carousel = new RentalCarousel();
        carousel.setImageUrl(url);
        carousel.setShowCity(city);
        carousel.setCreatedTime(new Date());
        carousel.setUpdatedTime(new Date());
        rentalCarouselMapper.insert(carousel);
        return carousel;
    }


    /** 
     * 更新数据
     *
     * @param rentalCarousel 实例对象
     * @return 实例对象
     */
    public RentalCarousel update(RentalCarousel rentalCarousel){
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalCarousel> chainWrapper = new LambdaUpdateChainWrapper<RentalCarousel>(rentalCarouselMapper);
        if(StrUtil.isNotBlank(rentalCarousel.getShowCity())){
            chainWrapper.eq(RentalCarousel::getShowCity, rentalCarousel.getShowCity());
        }
        if(StrUtil.isNotBlank(rentalCarousel.getImageUrl())){
            chainWrapper.eq(RentalCarousel::getImageUrl, rentalCarousel.getImageUrl());
        }
        if(Objects.nonNull(rentalCarousel.getStatus())){
            chainWrapper.eq(RentalCarousel::getStatus, rentalCarousel.getStatus());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalCarousel::getCarouselId, rentalCarousel.getCarouselId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if(ret){
            return queryById(rentalCarousel.getCarouselId());
        }else{
            return rentalCarousel;
        }
    }
    
    /** 
     * 通过主键删除数据
     *
     * @param carouselId 主键
     * @return 是否成功
     */
    public boolean deleteById(String carouselId){
        int total = rentalCarouselMapper.deleteById(carouselId);
        return total > 0;
    }

    public List<ResRentalCarousel> queryAll(Integer status){
        QueryWrapper<RentalCarousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("carousel_id", 0);
        queryWrapper.eq("status", status);
        List<RentalCarousel> rentalCarousels = rentalCarouselMapper.selectList(queryWrapper);

        Map<String, List<RentalCarousel>> rentalCarouselsByShowCity = rentalCarousels.stream()
                .collect(Collectors.groupingBy(RentalCarousel::getShowCity));


        List<ResRentalCarousel> result = Lists.newArrayList();

        rentalCarouselsByShowCity.forEach((k,v)->{
            ResRentalCarousel resRentalCarousel = new ResRentalCarousel();
            resRentalCarousel.setCity(k);
            resRentalCarousel.setRentalCarousels(v);
            result.add(resRentalCarousel);
        });

        return result;
    }

     public List<String> queryByIp(String ipAddress) {
         List<RentalCarousel> rentalCarousels = getActiveCarousels();

         List<String> result = findCarouselsByLocation(ipAddress, rentalCarousels);

         if (result.isEmpty()) {
             result = getDefaultCarousels(rentalCarousels);
         }

         return result;
     }

    private List<RentalCarousel> getActiveCarousels() {
        QueryWrapper<RentalCarousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("carousel_id", 0);
        List<RentalCarousel> rentalCarousels = rentalCarouselMapper.selectList(queryWrapper);

        return rentalCarousels.stream()
                .filter(carousel -> carousel.getStatus().equals(CarouselStatus.NORMAL.getStatus()))
                .collect(Collectors.toList());
    }

    private List<String> getDefaultCarousels(List<RentalCarousel> rentalCarousels) {
        return rentalCarousels.stream()
                .filter(carousel -> carousel.getStatus().equals(CarouselStatus.DEFAULT.getStatus()))
                .map(RentalCarousel::getImageUrl)
                .collect(Collectors.toList());
    }

    private List<String> findCarouselsByLocation(String ipAddress, List<RentalCarousel> rentalCarousels) {
        try {
            Location location = ipService.getLocation(ipAddress);
            if (location != null) {
                String city = location.getCity();
                return rentalCarousels.stream()
                        .filter(company -> city.contains(company.getShowCity()))
                        .map(RentalCarousel::getImageUrl)
                        .collect(Collectors.toList());
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return Collections.emptyList();
    }

}