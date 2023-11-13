
package com.hzwl.rental.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.user.RentalCarImages;
import com.hzwl.rental.service.user.RentalCarImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author GA666666
 * @Date  2023/8/30 12:43
 */
@Api(tags = "车辆图片对象功能接口")
@RestController
@RequestMapping("/rentalCarImages")
public class RentalCarImagesController {

    @Autowired
    private RentalCarImagesService rentalCarImagesService;

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{imageid}")
    public Result<RentalCarImages> queryById(String imageId) {
        return Result.success(rentalCarImagesService.queryById(imageId));
    }

     @ApiOperation("通过ID查询单条数据")
     @PostMapping("upload")
     public Result<RentalCarImages> upload(@RequestParam("file") MultipartFile file,@RequestParam("type") String type, @RequestParam("carId")String carId) {
         return Result.success();
     }



    @ApiOperation("分页查询")
    @GetMapping
    public Result paginQuery(@RequestBody RentalCarImages rentalCarImages, @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime) {
        Page<RentalCarImages> pageResult = rentalCarImagesService.queryEquities(rentalCarImages, pageNum, pageSize, startTime, endTime);
        return Result.success(pageResult);
    }

    @ApiOperation("新增数据")
    @PostMapping("/add")
    public Result<Boolean> add(RentalCarImages rentalCarImages) {
        return Result.success(rentalCarImagesService.insert(rentalCarImages));
    }

    @ApiOperation("更新数据")
    @PostMapping("/update")
    public Result<RentalCarImages> edit(RentalCarImages rentalCarImages) {
        return Result.success(rentalCarImagesService.update(rentalCarImages));
    }

    @ApiOperation("通过主键删除数据")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String imageId) {
        return Result.success(rentalCarImagesService.deleteById(imageId));
    }
}