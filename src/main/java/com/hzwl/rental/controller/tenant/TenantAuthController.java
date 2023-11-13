package com.hzwl.rental.controller.tenant;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.ReqRentalTenantUser;
import com.hzwl.rental.entity.tenant.RentalTenantUser;
import com.hzwl.rental.service.tenant.RentalTenantUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/9/3 19:51
 */

@Api(tags = "租户权限功能接口")
@Validated
@RestController
@RequestMapping("tenant_auth")
public class TenantAuthController {

    @Autowired
    private RentalTenantUserService rentalTenantUserService;

    @ApiOperation("注册用户")
    @PostMapping("register")
    public Result<String> register(@RequestBody @NotNull RentalTenantUser rentalTenantUser) {

        Assert.notBlank(rentalTenantUser.getTenantPassword(), ErrorCode.TENNAT_PASS_NOT_BLANK.getErrorMsg());
        Assert.notBlank(rentalTenantUser.getTenantName(), ErrorCode.TENNAT_USERNAME_NOT_BLANK.getErrorMsg());
        Assert.notBlank(rentalTenantUser.getCompanyId(), ErrorCode.TENNAT_COMPANY_ID_NOT_BLANK.getErrorMsg());

        String result = rentalTenantUserService.register(rentalTenantUser);
        return Result.success(result);
    }


    @ApiOperation("登录")
    @PostMapping("login")
    public Result<RentalTenantUser> login(@RequestBody @NotNull @Valid ReqRentalTenantUser rentalTenantUser) {

        RentalTenantUser result = rentalTenantUserService.login(RentalTenantUser.builder().tenantName(rentalTenantUser.getUsername()).tenantPassword(rentalTenantUser.getPassword()).build());
        result.setToken(StpUtil.getTokenInfo());
        return Result.success(result);
    }


    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{tenantid}")
    public Result<RentalTenantUser> queryById(String tenantId) {
        return Result.success(rentalTenantUserService.queryById(tenantId));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalTenantUser rentalTenantUser, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalTenantUser> pageResult = rentalTenantUserService.queryEquities(rentalTenantUser, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalTenantUser> add(RentalTenantUser rentalTenantUser) {
        return Result.success(rentalTenantUserService.insert(rentalTenantUser));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalTenantUser> edit(RentalTenantUser rentalTenantUser) {
        return Result.success(rentalTenantUserService.update(rentalTenantUser));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String tenantId) {
        return Result.success(rentalTenantUserService.deleteById(tenantId));
    }
}
