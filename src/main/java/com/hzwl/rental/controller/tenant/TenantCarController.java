package com.hzwl.rental.controller.tenant;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.dto.ReqRentalCarModels;
import com.hzwl.rental.entity.user.RentalCarModels;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.service.tenant.RentalTenantCarService;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author GA666666
 * @Date 2023/9/3 21:30
 */
@Api(tags = "租户车辆权限功能接口")
@Validated
@RestController
@RequestMapping("tenant_car")
public class TenantCarController {

    @Autowired
    private RentalTenantUserService rentalTenantUserService;

    @Autowired
    private RentalTenantCarService rentalTenantCarService;


    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalCars> add(@RequestBody @Validated RentalCars rentalCars) {
        return Result.success(rentalTenantCarService.insert(rentalCars));
    }


    @ApiOperation("上传图片")
    @PostMapping("/add-image")
    public Result uploadImage(@NotNull @RequestParam("file") MultipartFile file,
                              @NotNull @RequestParam("type") Integer imageType,
                              @NotBlank @RequestParam("carId") String carId) throws IOException, ExecutionException, InterruptedException {
        return Result.success(rentalTenantCarService.uploadImage(file, imageType, carId));
    }

    @ApiOperation("新增型号")
    @SaCheckLogin
    @PostMapping("/add-model")
    public Result insertModel(@Valid @NotNull @RequestBody ReqRentalCarModels carModels) {
        return Result.success(rentalTenantCarService.insertModel(carModels));
    }

    @ApiOperation("更新数据")
    @SaCheckLogin
    @PostMapping("/update")
    public Result<Boolean> edit(RentalCars rentalCars) {
        return Result.success(rentalTenantCarService.update(rentalCars));
    }

    @ApiOperation("通过主键删除数据")
    @SaCheckLogin
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String carId) {
        return Result.success(rentalTenantCarService.deleteById(carId));
    }


}
