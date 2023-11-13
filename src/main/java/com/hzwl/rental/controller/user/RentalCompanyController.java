
package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalCompany;
import com.hzwl.rental.entity.dto.ResRentalCompanyCars;
import com.hzwl.rental.service.user.RentalCompanyService;
import com.hzwl.rental.utils.EnvironmentUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Api(tags = "公司名称对象功能接口")
@RestController
@RequestMapping("/rentalCompany")
public class RentalCompanyController {

    @Autowired
    private RentalCompanyService rentalCompanyService;


    @Autowired
    private EnvironmentUtils environmentUtils;


    @SaCheckLogin
    @ApiOperation("通过IP查询公司信息")
    @GetMapping("/queryAll")
    public Result queryCompanyCars(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (environmentUtils.isDev()) {
            ipAddress = "61.48.40.214";
        }

        List<ResRentalCompanyCars> res = rentalCompanyService.queryCompanyCars(ipAddress);
        return Result.success(res);
    }


    @SaCheckLogin
    @ApiOperation("通过IP查询公司信息")
    @PostMapping("/queryAllByAdmin")
    public Result<List<RentalCompany>> queryAllByAdmin() {
        List<RentalCompany> res = rentalCompanyService.queryAllByAdmin();
        return Result.success(res);
    }


    @SaCheckLogin
    @ApiOperation("通过IP查询公司信息")
    @GetMapping("/queryByCompanyId")
    public Result queryByCompanyId(@RequestParam("companyId") @NotNull String companyId) {
        List<ResRentalCompanyCars> res = rentalCompanyService.queryByCompanyId(companyId);
        return Result.success(res);
    }


    @SaCheckLogin
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{companyId}")
    public Result<RentalCompany> queryById(@PathVariable("companyId") String companyId) {
        return Result.success(rentalCompanyService.queryById(companyId));
    }

    @ApiOperation("分页查询")
    @SaCheckLogin
    @GetMapping
    public Result paginQuery(@RequestBody(required = false) RentalCompany rentalCompany, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String companyName,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCompany> pageResult = rentalCompanyService.queryEquities(rentalCompany, pageNum, pageSize, startTime, endTime,companyName);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalCompany> add(RentalCompany rentalCompany) {
        return Result.success(rentalCompanyService.insert(rentalCompany));
    }

    @ApiOperation("更新数据")
    @SaCheckLogin
    @PostMapping("/update")
    public Result<RentalCompany> edit(@RequestBody RentalCompany rentalCompany) {
        return Result.success(rentalCompanyService.update(rentalCompany));
    }

    @ApiOperation("通过主键删除数据")
    @SaCheckLogin
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String companyId) {
        return Result.success(rentalCompanyService.deleteById(companyId));
    }
}