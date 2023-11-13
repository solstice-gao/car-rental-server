
package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.dto.ReqColorModel;
import com.hzwl.rental.entity.dto.ResRentalCars;
import com.hzwl.rental.entity.user.RentalCarModels;
import com.hzwl.rental.entity.user.RentalCars;
import com.hzwl.rental.service.user.RentalCarModelsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GA666666
 * @Date 2023/8/30 12:43
 */
@Api(tags = "对象功能接口")
@RestController
@RequestMapping("/rentalCarModels")
public class RentalCarModelsController {

    @Autowired
    private RentalCarModelsService rentalCarModelsService;

    @ApiOperation("通过ID查询单条数据")
    @PostMapping("/addColorModels")
    @SaCheckLogin
    public Result<RentalCars> addColorModel(@RequestBody ReqColorModel reqColorModel) {
        RentalCars car = rentalCarModelsService.addColorModel(reqColorModel);
        return Result.success(car);
    }


    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{modelid}")
    public Result<RentalCarModels> queryById(String modelId) {
        return Result.success(rentalCarModelsService.queryById(modelId));
    }

    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalCarModels rentalCarModels, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Page<RentalCarModels> pageResult = rentalCarModelsService.queryEquities(rentalCarModels, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<Boolean> add(RentalCarModels rentalCarModels) {
        return Result.success(rentalCarModelsService.insert(rentalCarModels));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalCarModels> edit(RentalCarModels rentalCarModels) {
        return Result.success(rentalCarModelsService.update(rentalCarModels));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String modelId) {
        return Result.success(rentalCarModelsService.deleteById(modelId));
    }
}