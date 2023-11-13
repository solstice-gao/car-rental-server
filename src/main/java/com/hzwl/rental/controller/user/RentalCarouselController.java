package com.hzwl.rental.controller.user;

import java.io.IOException;
import java.util.List;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalCarousel;
import com.hzwl.rental.utils.EnvironmentUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hzwl.rental.service.user.RentalCarouselService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @Author GA666666
 * @Date 2023/10/10 12:43
 */
@Api(tags = "首页轮播图对象功能接口")
@RestController
@RequestMapping("/rentalCarousel")
public class RentalCarouselController {

    @Autowired
    private RentalCarouselService rentalCarouselService;


    @Autowired
    private EnvironmentUtils environmentUtils;


    @ApiOperation("通过ID查询单条数据")
    @GetMapping("queryByIP")
    public Result queryByIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (environmentUtils.isDev()) {
            ipAddress = "61.48.40.214";
        }
        return Result.success(rentalCarouselService.queryByIp(ipAddress));
    }

    @ApiOperation("通过ID查询单条数据")
    @SaCheckLogin
    @GetMapping("queryAll")
    public Result queryAll(@RequestParam("status") Integer status) {
        return Result.success(rentalCarouselService.queryAll(status));
    }


    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{carouselid}")
    public Result<RentalCarousel> queryById(String carouselId) {
        return Result.success(rentalCarouselService.queryById(carouselId));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalCarousel rentalCarousel, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCarousel> pageResult = rentalCarouselService.queryEquities(rentalCarousel, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<RentalCarousel> add(@NotNull @RequestParam("file") MultipartFile file,
                                      @NotNull @RequestParam("city") String city) throws IOException {
        return Result.success(rentalCarouselService.insert(file, city));
    }


    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalCarousel> edit(RentalCarousel rentalCarousel) {
        return Result.success(rentalCarouselService.update(rentalCarousel));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String carouselId) {
        return Result.success(rentalCarouselService.deleteById(carouselId));
    }
}