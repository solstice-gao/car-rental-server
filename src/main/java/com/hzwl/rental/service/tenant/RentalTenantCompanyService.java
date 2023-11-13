package com.hzwl.rental.service.tenant;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.constants.ImageStatus;
import com.hzwl.rental.entity.dto.ReqRentalCompany;
import com.hzwl.rental.entity.tenant.RentalTenantUser;
import com.hzwl.rental.entity.user.RentalCarImages;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.entity.user.RentalCompany;
import com.hzwl.rental.service.user.RentalCarImagesService;
import com.hzwl.rental.service.user.RentalCarouselService;
import com.hzwl.rental.service.user.RentalCompanyService;
import com.hzwl.rental.utils.RAssert;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class RentalTenantCompanyService {

    @Autowired
    private RentalTenantUserService rentalTenantUserService;

    @Autowired
    private RentalCarouselService rentalCarouselService;


    @Autowired
    private RentalCompanyService rentalCompanyService;

    @Autowired
    private RentalCarImagesService rentalCarImagesService;

    @Autowired
    private FileUploadService fileUploadService;


    public RentalCompany add(ReqRentalCompany company){
        RentalCarImages icon = rentalCarImagesService.queryById(company.getCompanyIcon());
        RAssert.nonNull(icon, ErrorCode.ICON_NOT_EXIST);
        RentalCarImages businessLicense = rentalCarImagesService.queryById(company.getBusinessLicense());
        RAssert.nonNull(businessLicense, ErrorCode.LICENSE_NOT_EXIST);

        RentalCompany rentalCompany = new RentalCompany();

        rentalCompany.setCompanyName(company.getCompanyName());
        rentalCompany.setCompanyCity(company.getCompanyCity());
        rentalCompany.setCompanyIcon(icon.getImageUrl());
        rentalCompany.setBusinessLicense(businessLicense.getImageUrl());

        RentalCompany insert = rentalCompanyService.insert(rentalCompany);
        return insert;
    }


    public String uploadImage(MultipartFile file, Integer imageType) throws IOException, ExecutionException, InterruptedException {

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = (String) tokenInfo.getLoginId();
        Assert.isTrue(tokenInfo.getIsLogin(), ErrorCode.TOKEN_EXPIRATION.getErrorMsg());
        RentalTenantUser tenantUser = rentalTenantUserService.queryById(tokenValue);
        RAssert.nonNull(tenantUser, ErrorCode.TENNAT_IS_BLANK);
        String url = fileUploadService.uploadFile(file);


        RentalCarImages images = new RentalCarImages();
        images.setImageUrl(url);
        images.setStatus(imageType);
        images.setUpdatedTime(new Date());
        images.setCreatedTime(new Date());
        boolean insert = rentalCarImagesService.insert(images);
        RAssert.isTrue(insert, ErrorCode.INSERT_CAR_IMAGE_ERROR);
        return images.getImageId();
    }
}
