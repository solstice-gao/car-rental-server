package com.hzwl.rental.service.tenant;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.CarStatus;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.ReqRentalCarModels;
import com.hzwl.rental.entity.dto.ResRentalCars;
import com.hzwl.rental.entity.tenant.RentalTenantUser;
import com.hzwl.rental.entity.user.RentalCarImages;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.service.user.RentalCarImagesService;
import com.hzwl.rental.service.user.RentalCarModelsService;
import com.hzwl.rental.service.user.RentalCarsService;
import com.hzwl.rental.utils.RAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author GA666666
 * @Date 2023/9/3 21:42
 */
@Service
public class RentalTenantCarService {

    @Autowired
    private RentalCarsService rentalCarsService;

    @Autowired
    private RentalTenantUserService rentalTenantUserService;


    @Autowired
    private RentalCarImagesService rentalCarImagesService;

    @Autowired
    private RentalCarModelsService rentalCarModelsService;

    @Autowired
    private FileUploadService fileUploadService;


    /**
     * 新增数据
     *
     * @param rentalCars 实例对象
     * @return 实例对象
     */
    public RentalCars insert(RentalCars rentalCars) {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = (String) tokenInfo.getLoginId();
        Assert.isTrue(tokenInfo.getIsLogin(), ErrorCode.TOKEN_EXPIRATION.getErrorMsg());
        RentalTenantUser tenantUser = rentalTenantUserService.queryById(tokenValue);
        if (Objects.isNull(tenantUser)) {
            throw new BizException(ErrorCode.TENNAT_IS_BLANK);
        }
        rentalCars.setStatus(CarStatus.PENDING_REVIEW.getStatus());
        rentalCars.setCompanyId(tenantUser.getCompanyId());
        rentalCars.setCreatedTime(new Date());
        rentalCars.setUpdatedTime(new Date());
        rentalCarsService.insert(rentalCars);
        return rentalCars;
    }

    /**
     * 更新数据
     *
     * @param rentalCars 实例对象
     * @return 实例对象
     */
    public boolean update(RentalCars rentalCars) {
        return rentalCarsService.update(rentalCars);
    }

    /**
     * 通过主键删除数据
     *
     * @param carId 主键
     * @return 是否成功
     */
    public boolean deleteById(String carId) {
        return rentalCarsService.deleteById(carId);
    }

    public String uploadImage(MultipartFile file, Integer imageType, String carId) throws IOException, ExecutionException, InterruptedException {

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = (String) tokenInfo.getLoginId();
        Assert.isTrue(tokenInfo.getIsLogin(), ErrorCode.TOKEN_EXPIRATION.getErrorMsg());
        RentalTenantUser tenantUser = rentalTenantUserService.queryById(tokenValue);
        RAssert.nonNull(tenantUser, ErrorCode.TENNAT_IS_BLANK);

        RentalCars rentalCars = rentalCarsService.queryById(carId);
        RAssert.nonNull(rentalCars, ErrorCode.NOT_FOUND_CAR_INFO);
        String url = fileUploadService.uploadFile(file);
        if (0 == imageType) {
            rentalCars.setImageUrl(url);
            rentalCarsService.update(rentalCars);
            return rentalCars.getCarId();
        }

        RentalCarImages images = new RentalCarImages();
        images.setCarId(carId);
        images.setImageUrl(url);
        images.setStatus(imageType);
        images.setUpdatedTime(new Date());
        images.setCreatedTime(new Date());
        boolean insert = rentalCarImagesService.insert(images);
        RAssert.isTrue(insert, ErrorCode.INSERT_CAR_IMAGE_ERROR);
        return images.getImageId();
    }

    public Boolean insertModel(ReqRentalCarModels carModels) {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = (String) tokenInfo.getLoginId();
        Assert.isTrue(tokenInfo.getIsLogin(), ErrorCode.TOKEN_EXPIRATION.getErrorMsg());
        RentalTenantUser tenantUser = rentalTenantUserService.queryById(tokenValue);
        RAssert.nonNull(tenantUser, ErrorCode.TENNAT_IS_BLANK);

        ResRentalCars resRentalCars = rentalCarsService.queryResById(carModels.getCarId());
        RAssert.nonNull(resRentalCars, ErrorCode.NOT_FOUND_CAR_INFO);
        RAssert.isTrue(tenantUser.getCompanyId().equals(resRentalCars.getCompanyInfo().getCompanyId()), ErrorCode.TENNAT_VERIFY_ERROR);

        AtomicReference<Integer> counter = new AtomicReference<>(0);
        carModels.getModelsList().forEach(rentalCarModels -> {
            rentalCarModels.setCarId(resRentalCars.getCarId());
            rentalCarModels.setCreatedTime(new Date());
            rentalCarModels.setUpdatedTime(new Date());
            boolean insert = rentalCarModelsService.insert(rentalCarModels);
            if (insert) {
                counter.getAndSet(counter.get() + 1);
            }
        });
        RAssert.isTrue(counter.get() == carModels.getModelsList().size(), ErrorCode.INSERT_CAR_MODEL_ERROR);
        return counter.get() == carModels.getModelsList().size();
    }
}
