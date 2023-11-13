package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.entity.dto.*;
import com.hzwl.rental.entity.user.RentalCoupons;
import com.hzwl.rental.entity.user.RentalOrder;
import com.hzwl.rental.service.user.RentalOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Api(tags = "订单对象功能接口")
@RestController
@RequestMapping("/rentalOrder")
public class RentalOrderController {
    @Autowired
    private RentalOrderService rentalOrderService;


    /**
     * 创建预付单
     *
     * @return
     */
    @SaCheckLogin
    @PostMapping("/preOrder")
    public Result createPreOrder(@RequestBody @Valid ReqCreatePreOrder preOrder) {
        PayResponeData order = rentalOrderService.createPreOrder(preOrder);
        return Result.success(order);

    }

    /**
     * 查询订单状态
     *
     * @return
     */
    @SaCheckLogin
    @PostMapping("/queryOrderStatus")
    public Result queryOrderStatus(@RequestBody @Valid PayStatusRequest statusRequest) {
        PayStatusRespone order = rentalOrderService.queryOrderStatus(statusRequest);
        return Result.success(order);
    }


    /**
     * 查询订单状态
     *
     * @return
     */
    @SaCheckLogin
    @PostMapping("updateOrderStatus")
    public Result updateOrderStatus(@RequestBody ReqRentalOrderStatus orderStatus ) {
        return Result.success(rentalOrderService.updateOrderStatus(orderStatus));
    }


    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 实例对象
     */
    @ApiOperation("通过ID查询单条数据")
    @SaCheckLogin
    @GetMapping("{orderid}")
    public Result<RentalOrder> queryById(String orderId) {
        return Result.success(rentalOrderService.queryById(orderId));
    }

    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 实例对象
     */
    @ApiOperation("通过ID查询单条数据")
    @SaCheckLogin
    @GetMapping("queryInfoById/{orderId}")
    public Result<ResOrderInfo> queryInfoById(@PathVariable("orderId") @NotNull String orderId) throws ExecutionException, InterruptedException {
        return Result.success(rentalOrderService.queryInfoById(orderId));
    }


    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    @ApiOperation("通过ID查询单条数据")
    @SaCheckLogin
    @GetMapping("getOrders")
    public Result<List<ResRentalOrder>> getOrderList() throws ExecutionException, InterruptedException {
        return Result.success(rentalOrderService.getOrderList());
    }

    @ApiOperation("通过ID查询单条数据")
    @SaCheckLogin
    @PostMapping("getOrdersAdmin")
    public Result<IPage<ResRentalOrder>> getOrdersAdmin(HttpServletRequest request , @RequestBody RentalOrder rentalOrder, @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize) throws ExecutionException, InterruptedException {
        return Result.success(rentalOrderService.getOrdersAdmin(rentalOrder, page, pageSize));
    }


    /**
     * 新增数据
     *
     * @param rentalOrder 实例对象
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @SaCheckLogin
    @PostMapping
    public ResponseEntity<RentalOrder> add(RentalOrder rentalOrder) {
        return ResponseEntity.ok(rentalOrderService.insert(rentalOrder));
    }

    /**
     * 更新数据
     *
     * @param rentalOrder 实例对象
     * @return 实例对象
     */
    @ApiOperation("更新数据")
    @SaCheckLogin
    @PutMapping
    public ResponseEntity<RentalOrder> edit(RentalOrder rentalOrder) {
        return ResponseEntity.ok(rentalOrderService.update(rentalOrder));
    }

    /**
     * 通过主键删除数据
     *
     * @param orderId 主键
     * @return 是否成功
     */
    @ApiOperation("通过主键删除数据")
    @SaCheckLogin
    @DeleteMapping("del")
    public Result deleteById(@RequestParam String orderId) {
        return Result.success(rentalOrderService.deleteById(orderId));
    }
}