package com.hzwl.rental.controller.user;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalCoupons;
import com.hzwl.rental.service.user.RentalCouponsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Api(tags = "优惠券对象功能接口")
@RestController
@SaCheckLogin
@RequestMapping("/rentalCoupons")
public class RentalCouponsController {

    @Autowired
    private RentalCouponsService rentalCouponsService;

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{couponid}")
    public Result<RentalCoupons> queryById(String couponId) {
        return Result.success(rentalCouponsService.queryById(couponId));
    }

    @SaCheckLogin
    @ApiOperation("查询可领优惠券数据")
    @GetMapping("all")
    public Result<List<RentalCoupons>> queryAll(String couponId) {
        return Result.success(rentalCouponsService.queryAll());
    }


    @ApiOperation("分页查询")
    @PostMapping
    public Result paginQuery(@RequestBody(required = false) RentalCoupons rentalCoupons, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false,name = "startTime") String startTime,
                             @RequestParam(required = false,name = "endTime") String endTime) {
        Page<RentalCoupons> pageResult = rentalCouponsService.queryEquities(rentalCoupons, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    @SaCheckLogin
    public Result<RentalCoupons> add(@RequestBody RentalCoupons rentalCoupons) {
        return Result.success(rentalCouponsService.insert(rentalCoupons));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    @SaCheckLogin
    public Result<RentalCoupons> edit(RentalCoupons rentalCoupons) {
        return Result.success(rentalCouponsService.update(rentalCoupons));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String couponId) {
        return Result.success(rentalCouponsService.deleteById(couponId));
    }
}