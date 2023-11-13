package com.hzwl.rental.controller.tenant;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.dto.ReqRentalCarModels;
import com.hzwl.rental.entity.dto.ReqRentalCompany;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.service.tenant.RentalTenantCarService;
import com.hzwl.rental.service.tenant.RentalTenantCompanyService;
import com.hzwl.rental.service.tenant.RentalTenantUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @Author GA666666
 * @Date 2023/9/3 21:30
 */
@Api(tags = "租户车辆权限功能接口")
@Validated
@RestController
@RequestMapping("tenant_company")
public class TenantCompanyController {

    @Autowired
    private RentalTenantUserService rentalTenantUserService;

    @Autowired
    private RentalTenantCompanyService rentalTenantCompanyService;


    @ApiOperation("新增公司")
    @SaCheckLogin
    @PostMapping("/add")
    public Result add(@RequestBody @Validated ReqRentalCompany company) {
        return Result.success(rentalTenantCompanyService.add(company));
    }


    @ApiOperation("上传图片")
    @SaCheckLogin
    @PostMapping("/add-image")
    public Result uploadImage(@NotNull @RequestParam("file") MultipartFile file,
                              @NotNull @RequestParam("type") Integer imageType
    ) throws IOException, ExecutionException, InterruptedException {
        return Result.success(rentalTenantCompanyService.uploadImage(file, imageType));
    }


}
