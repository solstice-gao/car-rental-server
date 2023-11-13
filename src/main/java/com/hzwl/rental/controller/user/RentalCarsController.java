
package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.entity.dto.ResRentalCars;
import com.hzwl.rental.service.user.RentalCarsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Api(tags = "车辆信息对象功能接口")
@RestController
@RequestMapping("/rentalCars")
public class RentalCarsController {

    @Autowired
    private RentalCarsService rentalCarsService;


    @SaCheckLogin
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{carId}")
    public Result<ResRentalCars> queryById(@PathVariable("carId") String carId) {
        return Result.success(rentalCarsService.queryResById(carId));
    }

    @SaCheckLogin
    @ApiOperation("下架商品")
    @GetMapping("offTheShelf")
    public Result offTheShelf(@RequestParam String carId,@RequestParam(defaultValue = "1") Integer status) {
        return Result.success(rentalCarsService.offTheShelf(carId,status));
    }


    @SaCheckLogin
    @ApiOperation("queryGroupByCompany")
    @GetMapping("queryGroupByCompany")
    public Result queryGroupByCompany(@RequestParam(defaultValue = "1") String status) throws ExecutionException, InterruptedException {
        return Result.success(rentalCarsService.queryGroupByCompany(status));
    }


    @SaCheckLogin
    @ApiOperation("通过ID查询单条数据")
    @PostMapping("putShelves/{carId}")
    public Result<Boolean> putShelves(@PathVariable("carId") String carId) {
        return Result.success(rentalCarsService.putShelves(carId));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody(required = false) RentalCars rentalCars, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "1000") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCars> pageResult = rentalCarsService.queryEquities(rentalCars, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }
}