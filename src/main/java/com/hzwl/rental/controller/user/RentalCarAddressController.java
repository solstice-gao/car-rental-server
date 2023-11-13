
package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalCarAddress;
import com.hzwl.rental.service.user.RentalCarAddressService;
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
@RequestMapping("/rentalCarAddress")
public class RentalCarAddressController {

    @Autowired
    private RentalCarAddressService rentalCarAddressService;

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{addressid}")
    public Result<RentalCarAddress> queryById(String addressId) {
        return Result.success(rentalCarAddressService.queryById(addressId));
    }


    @SaCheckLogin
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("queryAll")
    public Result queryAll(@RequestParam String id) {
        return Result.success(rentalCarAddressService.queryByCarId(id));
    }

    @ApiOperation("分页查询")
//    @GetMapping
    public Result paginQuery(@RequestBody RentalCarAddress rentalCarAddress, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCarAddress> pageResult = rentalCarAddressService.queryEquities(rentalCarAddress, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @SaCheckLogin
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalCarAddress> add(@RequestBody @Valid RentalCarAddress rentalCarAddress) {
        return Result.success(rentalCarAddressService.insert(rentalCarAddress));
    }

    @SaCheckLogin
    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalCarAddress> edit(RentalCarAddress rentalCarAddress) {
        return Result.success(rentalCarAddressService.update(rentalCarAddress));
    }

    @SaCheckLogin
    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String addressId) {
        return Result.success(rentalCarAddressService.deleteById(addressId));
    }
}