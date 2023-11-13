package com.hzwl.rental.controller.user;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.dto.ReqRentalCouponsCompany;
import com.hzwl.rental.entity.user.RentalCoupons;
import com.hzwl.rental.entity.user.RentalCouponsRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hzwl.rental.entity.user.RentalCouponsCompany;
import com.hzwl.rental.service.user.RentalCouponsCompanyService;

/**
 * @Author GA666666
 * @Date 2023/10/3 12:43
 */
@Api(tags = "优惠券可用公司对象功能接口")
@RestController
@RequestMapping("/rentalCouponsCompany")
public class RentalCouponsCompanyController {

    @Autowired
    private RentalCouponsCompanyService rentalCouponsCompanyService;

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{companycouponsid}")
    public Result<RentalCouponsCompany> queryById(String companyCouponsId) {
        return Result.success(rentalCouponsCompanyService.queryById(companyCouponsId));
    }


    @ApiOperation("通过公司ID查询单条数据")
    @PostMapping("queryByCompany")
    @SaCheckLogin
    public Result<List<RentalCoupons>> queryByCompany(@RequestBody ReqRentalCouponsCompany couponsCompany) {
        return Result.success(rentalCouponsCompanyService.queryByCompany(couponsCompany));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalCouponsCompany rentalCouponsCompany, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCouponsCompany> pageResult = rentalCouponsCompanyService.queryEquities(rentalCouponsCompany, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalCouponsCompany> add(RentalCouponsCompany rentalCouponsCompany) {
        return Result.success(rentalCouponsCompanyService.insert(rentalCouponsCompany));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalCouponsCompany> edit(RentalCouponsCompany rentalCouponsCompany) {
        return Result.success(rentalCouponsCompanyService.update(rentalCouponsCompany));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String companyCouponsId) {
        return Result.success(rentalCouponsCompanyService.deleteById(companyCouponsId));
    }
}