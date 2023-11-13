package com.hzwl.rental.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalDeliveryAddress;
import com.hzwl.rental.service.user.RentalDeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Author GA666666
 * @Date  2023/9/14 12:43
 */
@Api(tags = "提货地址对象功能接口")
@RestController
@RequestMapping("/rentalDeliveryAddress")
public class RentalDeliveryAddressController {

    @Autowired
    private RentalDeliveryAddressService rentalDeliveryAddressService;

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{deliveryid}")
    public Result<RentalDeliveryAddress> queryById(String deliveryId) {
        return Result.success(rentalDeliveryAddressService.queryById(deliveryId));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalDeliveryAddress rentalDeliveryAddress, @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime) {
        Page<RentalDeliveryAddress> pageResult = rentalDeliveryAddressService.queryEquities(rentalDeliveryAddress, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalDeliveryAddress> add(RentalDeliveryAddress rentalDeliveryAddress) {
        return Result.success(rentalDeliveryAddressService.insert(rentalDeliveryAddress));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalDeliveryAddress> edit(RentalDeliveryAddress rentalDeliveryAddress) {
        return Result.success(rentalDeliveryAddressService.update(rentalDeliveryAddress));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String deliveryId) {
        return Result.success(rentalDeliveryAddressService.deleteById(deliveryId));
    }
}