package com.hzwl.rental.controller.user;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.dto.ReqRentalCoupons;
import com.hzwl.rental.entity.user.RentalCouponsRecord;
import com.hzwl.rental.service.user.RentalCouponsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Api(tags = "优惠券记录对象功能接口")
@RestController
@RequestMapping("/rentalCouponsRecord")
public class RentalCouponsRecordController {

    @Autowired
    private RentalCouponsRecordService rentalCouponsRecordService;


    @ApiOperation("领取优惠券")
    @PostMapping("receive")
    @SaCheckLogin
    public Result<RentalCouponsRecord> receive(@RequestBody ReqRentalCoupons rentalCoupons) {
        return Result.success(rentalCouponsRecordService.receive(rentalCoupons));
    }


    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{recordid}")
    public Result<RentalCouponsRecord> queryById(String recordId) {
        return Result.success(rentalCouponsRecordService.queryById(recordId));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalCouponsRecord rentalCouponsRecord, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCouponsRecord> pageResult = rentalCouponsRecordService.queryEquities(rentalCouponsRecord, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalCouponsRecord> add(RentalCouponsRecord rentalCouponsRecord) {
        return Result.success(rentalCouponsRecordService.insert(rentalCouponsRecord));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalCouponsRecord> edit(RentalCouponsRecord rentalCouponsRecord) {
        return Result.success(rentalCouponsRecordService.update(rentalCouponsRecord));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String recordId) {
        return Result.success(rentalCouponsRecordService.deleteById(recordId));
    }
}