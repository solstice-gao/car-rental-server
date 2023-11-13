
package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalUserAddress;
import com.hzwl.rental.service.user.RentalUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Api(tags = "用户地址对象功能接口")
@RestController
@RequestMapping("/rentalUserAddress")
public class RentalUserAddressController {

    @Autowired
    private RentalUserAddressService rentalUserAddressService;

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{addressid}")
    public Result<RentalUserAddress> queryById(String addressId) {
        return Result.success(rentalUserAddressService.queryById(addressId));
    }


    @SaCheckLogin
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("queryAll")
    public Result queryAll(String addressId) {
        return Result.success(rentalUserAddressService.queryAll());
    }

    @ApiOperation("分页查询")
//    @GetMapping
    public Result paginQuery(@RequestBody RentalUserAddress rentalUserAddress, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalUserAddress> pageResult = rentalUserAddressService.queryEquities(rentalUserAddress, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @SaCheckLogin
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalUserAddress> add(@RequestBody @Valid RentalUserAddress rentalUserAddress) {
        return Result.success(rentalUserAddressService.insert(rentalUserAddress));
    }

    @SaCheckLogin
    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalUserAddress> edit(RentalUserAddress rentalUserAddress) {
        return Result.success(rentalUserAddressService.update(rentalUserAddress));
    }

    @SaCheckLogin
    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String addressId) {
        return Result.success(rentalUserAddressService.deleteById(addressId));
    }
}