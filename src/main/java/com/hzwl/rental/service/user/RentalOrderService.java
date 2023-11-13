package com.hzwl.rental.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.constants.*;
import com.hzwl.rental.entity.dto.*;
import com.hzwl.rental.entity.user.*;
import com.hzwl.rental.mapper.user.*;
import com.hzwl.rental.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;


@Service
public class RentalOrderService {
    @Autowired
    private RentalOrderMapper rentalOrderMapper;

    @Autowired
    private RentalCarModelsMapper rentalCarModelsMapper;

    @Autowired
    private RentalCouponsService rentalCouponsService;

    @Autowired
    private RentalCarAddressService rentalCarAddressService;


    @Autowired
    private RentalCarModelsService rentalCarModelsService;

    @Autowired
    private RentalCarsMapper rentalCarsMapper;

    @Autowired
    private RentalUserAddressMapper rentalUserAddressMapper;

    @Autowired
    private RentalUserMapper rentalUserMapper;

    @Autowired
    private RentalCarAddressMapper rentalCarAddressMapper;

    @Autowired
    private RentalCompanyService rentalCompanyService;

    @Autowired
    private RentalCarsService rentalCarsService;

    @Autowired
    private RentalCouponsRecordService rentalCouponsRecordService;

    @Autowired
    private EnvironmentUtils environmentUtils;

    @Autowired
    private ThreadPoolUtils threadPoolUtils;


    @Autowired
    private PayService payService;

    @Autowired
    private UserUtils userUtils;

    @Transactional(rollbackFor = {RuntimeException.class, BizException.class})
    public PayStatusRespone queryOrderStatus(PayStatusRequest statusRequest) {
        RentalUser userInfo = userUtils.getUserInfo();
        RAssert.isTrue(StringUtils.isNotBlank(statusRequest.getBusinessId()) || StringUtils.isNotBlank(statusRequest.getOrderId())
                , ErrorCode.QUERY_ORDER_STATUS_PARAMS_ERROR);
        LambdaQueryWrapper<RentalOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(statusRequest.getOrderId())) {
            queryWrapper.eq(RentalOrder::getOtherOrderId, statusRequest.getOrderId());
        }
        if (StrUtil.isNotBlank(statusRequest.getBusinessId())) {
            queryWrapper.eq(RentalOrder::getOrderNo, statusRequest.getBusinessId());
        }

        RentalOrder order = rentalOrderMapper.selectOne(queryWrapper);


        if (Objects.equals(order.getOrderStatus(), OrderStatus.COMPLETED.getStatus())) {
            PayStatusRespone payStatusRespone = new PayStatusRespone();
            payStatusRespone.setStatus(order.getOrderStatus());
            payStatusRespone.setId(order.getOtherOrderId());
            payStatusRespone.setLink(order.getLink());
            payStatusRespone.setAmount(order.getTotalPrice());
            return payStatusRespone;
        }

        RAssert.nonNull(order, ErrorCode.SIGNATURE_ERROR);

        PayStatusRespone payStatusRespone = payService.queryOrderStatus(statusRequest);
        RAssert.nonNull(order, ErrorCode.QUERY_ORDER_STATUS_ERROR);
        order.setOrderStatus(payStatusRespone.getStatus());
        rentalOrderMapper.updateById(order);
        return payStatusRespone;
    }

    @Transactional(rollbackFor = {RuntimeException.class, BizException.class})
    public PayResponeData createPreOrder(ReqCreatePreOrder preOrder) {
        String modelId = preOrder.getModelId();
        RentalCarModels rentalCarModels = rentalCarModelsMapper.selectById(modelId);
        RAssert.nonNull(rentalCarModels, ErrorCode.NOT_FOUND_CAR_MODELS);
        String addressId = preOrder.getAddressId();
        RentalCarAddress rentalUserAddress = rentalCarAddressMapper.selectById(addressId);
        RAssert.nonNull(rentalUserAddress, ErrorCode.NOT_FOUND_USER_ADRESS);

        if (StringUtils.isNotBlank(preOrder.getCouponId())) {
            RentalUser userInfo = userUtils.getUserInfo();
            RentalCouponsRecord record = rentalCouponsRecordService.queryNormalByUserId(userInfo.getUserId()).stream().filter(coupon -> coupon.getCouponsId().equals(preOrder.getCouponId())).findFirst().orElse(null);
            RAssert.nonNull(record, ErrorCode.NOT_FOUND_COUPON);
        }

        if (preOrder.getOrderType() == PayType.ALIPAY) {
            PayResponeData payResponeData = this.preAliPay(preOrder, rentalCarModels);
            return payResponeData;
        } else if (preOrder.getOrderType() == PayType.WECHAT) {
            this.preWeChatPay();
        }

        throw new BizException(ErrorCode.CREATE_PRE_ORDER_ERROR);
    }


    @Transactional(rollbackFor = {RuntimeException.class, BizException.class}, propagation = Propagation.REQUIRES_NEW)
    private PayResponeData preAliPay(ReqCreatePreOrder preOrder, RentalCarModels rentalCarModels) {
        RentalUser userInfo = userUtils.getUserInfo();


        BigDecimal sumPrice = rentalCarModels.getCarPrice().add(rentalCarModels.getCarBatteryFee())
                .add(rentalCarModels.getCarInitialFee())
                .add(rentalCarModels.getCarInsuranceFee())
                .add(rentalCarModels.getCarProcessingFee());
        BigDecimal totalPrice = sumPrice;
        RentalCoupons rentalCoupons = rentalCouponsService.queryById(preOrder.getCouponId());
        if (StringUtils.isNotBlank(preOrder.getCouponId())) {
            BigDecimal subtract = sumPrice.subtract(rentalCoupons.getDiscountValue());
            if (subtract.compareTo(BigDecimal.ZERO) <= 0) {
                sumPrice = BigDecimal.ZERO;
            } else {
                sumPrice = subtract;
            }
            RentalCouponsRecord record = rentalCouponsRecordService.queryNormalByUserId(userInfo.getUserId()).stream().filter(coupon -> coupon.getCouponsId().equals(preOrder.getCouponId())).findFirst().orElse(null);
            RAssert.nonNull(record, ErrorCode.NOT_FOUND_COUPON);
            record.setCouponsStatus(CouponsStatus.USED.getStatus());
            rentalCouponsRecordService.update(record);
        }

        BigDecimal payable = sumPrice.multiply(BigDecimal.valueOf(100));
        String orderId = "HZWL" + OrderUtils.generateOrderNumber();

        RentalCars rentalCars = rentalCarsMapper.selectById(rentalCarModels.getCarId());

        RentalOrder order = new RentalOrder();
        order.setOrderNo(orderId);
        order.setAddressId(preOrder.getAddressId());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT.getStatus());
        order.setCarId(rentalCarModels.getCarId());
        order.setModelId(rentalCarModels.getModelId());
        order.setCompanyId(rentalCars.getCompanyId());
        order.setUserId(userInfo.getUserId());
        order.setAddressId(preOrder.getAddressId());
        order.setTotalPrice(totalPrice.multiply(BigDecimal.valueOf(100)));
        order.setPayablePrice(payable);
        order.setCouponPrice(BigDecimal.ZERO);
        order.setPayMethod(PayType.ALIPAY.getCode());
        order.setLeaveComment(preOrder.getLeaveComment());
        order.setCreatedTime(new Date());
        order.setUpdatedTime(new Date());

        if (environmentUtils.isDev()) {
            payable = BigDecimal.ONE;
        }

        PayResponeData payResponeData = payService.preAliPay(orderId, payable, orderId);
        payResponeData.setOrderNo(orderId);
        order.setLink(payResponeData.getLink());
        order.setOtherOrderId(payResponeData.getOrderId());

        if (Objects.nonNull(rentalCoupons)) {
            order.setCouponId(rentalCoupons.getCouponId());
            order.setCouponPrice(rentalCoupons.getDiscountValue().multiply(BigDecimal.valueOf(100)));
        }
        rentalOrderMapper.insert(order);
        return payResponeData;

    }

    private String preWeChatPay() {
        return null;
    }


    /**
     * 获取订单详细信息
     *
     * @param orderId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ResOrderInfo queryInfoById(String orderId) throws ExecutionException, InterruptedException {
        RentalOrder order = rentalOrderMapper.selectById(orderId);
        RentalUser userInfo = userUtils.getUserInfo();
        String userId = order.getUserId();
        RAssert.isTrue(userId.equals(userInfo.getUserId()), ErrorCode.NO_INTERFACE_PERMISSION);

        ExecutorService executorService = threadPoolUtils.getExecutorService();
        String carId = order.getCarId();
        String modelId = order.getModelId();
        String couponId = order.getCouponId();
        String addressId = order.getAddressId();
        String companyId = order.getCompanyId();


        ResOrderInfo orderInfo = new ResOrderInfo();

        CompletableFuture<RentalCarModels> rentalCarModelsFuture = CompletableFuture.supplyAsync(() -> rentalCarModelsService.queryById(modelId), executorService);
        CompletableFuture<RentalCars> rentalCarsFuture = CompletableFuture.supplyAsync(() -> rentalCarsService.queryById(carId), executorService);
        CompletableFuture<RentalCompany> rentalCompanyFuture = CompletableFuture.supplyAsync(() -> rentalCompanyService.queryById(companyId), executorService);
        CompletableFuture<RentalCarAddress> rentalCarAdressFuture = CompletableFuture.supplyAsync(() -> rentalCarAddressService.queryById(addressId), executorService);
        CompletableFuture<RentalCoupons> rentalCouponsFuture = CompletableFuture.supplyAsync(() -> rentalCouponsService.queryById(couponId), executorService);
        CompletableFuture.allOf(rentalCarsFuture, rentalCompanyFuture, rentalCarModelsFuture, rentalCarAdressFuture, rentalCouponsFuture).join();

        orderInfo.setRentalOrder(order);
        orderInfo.setRentalCars(rentalCarsFuture.get());
        orderInfo.setRentalUser(userInfo);
        orderInfo.setRentalCompany(rentalCompanyFuture.get());
        orderInfo.setRentalCarModels(rentalCarModelsFuture.get());
        orderInfo.setRentalCoupons(rentalCouponsFuture.get());
        orderInfo.setRentalCarAddress(rentalCarAdressFuture.get());

        return orderInfo;
    }


    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 实例对象
     */
    public RentalOrder queryById(String orderId) {

        return rentalOrderMapper.selectById(orderId);
    }

    /**
     * 分页查询
     *
     * @param rentalOrder 筛选条件
     * @param current     当前页码
     * @param size        每页大小
     * @return
     */
    public Page<RentalOrder> paginQuery(RentalOrder rentalOrder, long current, long size) {
        //1. 构建动态查询条件
        LambdaQueryWrapper<RentalOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(rentalOrder.getUserId())) {
            queryWrapper.eq(RentalOrder::getUserId, rentalOrder.getUserId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getCarId())) {
            queryWrapper.eq(RentalOrder::getCarId, rentalOrder.getCarId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getAddressId())) {
            queryWrapper.eq(RentalOrder::getAddressId, rentalOrder.getAddressId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getCouponId())) {
            queryWrapper.eq(RentalOrder::getCouponId, rentalOrder.getCouponId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getPayMethod())) {
            queryWrapper.eq(RentalOrder::getPayMethod, rentalOrder.getPayMethod());
        }
        if (StrUtil.isNotBlank(rentalOrder.getInvoiceTplId())) {
            queryWrapper.eq(RentalOrder::getInvoiceTplId, rentalOrder.getInvoiceTplId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getLeaveComment())) {
            queryWrapper.eq(RentalOrder::getLeaveComment, rentalOrder.getLeaveComment());
        }
        if (Objects.nonNull(rentalOrder.getOrderStatus())) {
            queryWrapper.eq(RentalOrder::getOrderStatus, rentalOrder.getOrderStatus());
        }

        //2. 执行分页查询
        Page<RentalOrder> pagin = new Page<>(current, size, true);
        IPage<RentalOrder> selectResult = rentalOrderMapper.selectByPages(pagin, queryWrapper);
        pagin.setPages(selectResult.getPages());
        pagin.setTotal(selectResult.getTotal());
        pagin.setRecords(selectResult.getRecords());
        //3. 返回结果
        return pagin;
    }

    /**
     * 新增数据
     *
     * @param rentalOrder 实例对象
     * @return 实例对象
     */
    public RentalOrder insert(RentalOrder rentalOrder) {
        rentalOrderMapper.insert(rentalOrder);
        return rentalOrder;
    }

    /**
     * 更新数据
     *
     * @param rentalOrder 实例对象
     * @return 实例对象
     */
    public RentalOrder update(RentalOrder rentalOrder) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalOrder> chainWrapper = new LambdaUpdateChainWrapper<RentalOrder>(rentalOrderMapper);
        if (StrUtil.isNotBlank(rentalOrder.getUserId())) {
            chainWrapper.set(RentalOrder::getUserId, rentalOrder.getUserId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getCarId())) {
            chainWrapper.set(RentalOrder::getCarId, rentalOrder.getCarId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getAddressId())) {
            chainWrapper.set(RentalOrder::getAddressId, rentalOrder.getAddressId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getCouponId())) {
            chainWrapper.set(RentalOrder::getCouponId, rentalOrder.getCouponId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getPayMethod())) {
            chainWrapper.set(RentalOrder::getPayMethod, rentalOrder.getPayMethod());
        }
        if (StrUtil.isNotBlank(rentalOrder.getInvoiceTplId())) {
            chainWrapper.set(RentalOrder::getInvoiceTplId, rentalOrder.getInvoiceTplId());
        }
        if (StrUtil.isNotBlank(rentalOrder.getLeaveComment())) {
            chainWrapper.set(RentalOrder::getLeaveComment, rentalOrder.getLeaveComment());
        }
        if (Objects.nonNull(rentalOrder.getOrderStatus())) {
            chainWrapper.set(RentalOrder::getOrderStatus, rentalOrder.getOrderStatus());
        }

        //2. 设置主键，并更新
        chainWrapper.eq(RentalOrder::getOrderId, rentalOrder.getOrderId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalOrder.getOrderId());
        } else {
            return rentalOrder;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param orderId 主键
     * @return 是否成功
     */
    public boolean deleteById(String orderId) {
        int total = rentalOrderMapper.deleteById(orderId);
        return total > 0;
    }

    public IPage<ResRentalOrder> getOrdersAdmin(RentalOrder reqRentalOrder, long current, long size) throws ExecutionException, InterruptedException {
        LambdaQueryWrapper<RentalOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(RentalOrder::getOrderId, 0);
        List<String> rentalUsers;
        if (StrUtil.isNotBlank(reqRentalOrder.getPhone())) {
            QueryWrapper<RentalUser> wapper = new QueryWrapper();
            wapper.like("phone", reqRentalOrder.getPhone());
            rentalUsers = rentalUserMapper.selectList(wapper).stream().map(RentalUser::getUserId).collect(Collectors.toList());
        } else {
            rentalUsers = Collections.emptyList();
        }

        if (StrUtil.isNotBlank(reqRentalOrder.getUserId())) {
            queryWrapper.eq(RentalOrder::getUserId, reqRentalOrder.getUserId());
        }

        if (StrUtil.isNotBlank(reqRentalOrder.getOrderNo())) {
            queryWrapper.like(RentalOrder::getOrderNo, reqRentalOrder.getOrderNo());
        }

        if (StrUtil.isNotBlank(reqRentalOrder.getCarId())) {
            queryWrapper.eq(RentalOrder::getCarId, reqRentalOrder.getCarId());
        }
        if (StrUtil.isNotBlank(reqRentalOrder.getAddressId())) {
            queryWrapper.eq(RentalOrder::getAddressId, reqRentalOrder.getAddressId());
        }
        if (StrUtil.isNotBlank(reqRentalOrder.getCouponId())) {
            queryWrapper.eq(RentalOrder::getCouponId, reqRentalOrder.getCouponId());
        }
        if (StrUtil.isNotBlank(reqRentalOrder.getPayMethod())) {
            queryWrapper.eq(RentalOrder::getPayMethod, reqRentalOrder.getPayMethod());
        }
        if (StrUtil.isNotBlank(reqRentalOrder.getInvoiceTplId())) {
            queryWrapper.eq(RentalOrder::getInvoiceTplId, reqRentalOrder.getInvoiceTplId());
        }
        if (StrUtil.isNotBlank(reqRentalOrder.getLeaveComment())) {
            queryWrapper.eq(RentalOrder::getLeaveComment, reqRentalOrder.getLeaveComment());
        }
        if (Objects.nonNull(reqRentalOrder.getOrderStatus())) {
            queryWrapper.eq(RentalOrder::getOrderStatus, reqRentalOrder.getOrderStatus());
        }

        Page<RentalOrder> pagin = new Page<>(current, size, true);
        IPage<RentalOrder> selectResult = rentalOrderMapper.selectByPages(pagin, queryWrapper);

        List<RentalOrder> rentalOrders = selectResult.getRecords();
        if (!rentalUsers.isEmpty()) {
            rentalOrders = rentalOrders.stream().filter(order -> rentalUsers.contains(order.getUserId())).collect(Collectors.toList());
        }

        ExecutorService executorService = threadPoolUtils.getExecutorService();
        List<ResRentalOrder> result = Lists.newArrayList();
        for (RentalOrder rentalOrder : rentalOrders) {
            ResRentalOrder resRentalOrder = new ResRentalOrder();
            String carId = rentalOrder.getCarId();
            String companyId = rentalOrder.getCompanyId();
            String modelId = rentalOrder.getModelId();
            String userId = rentalOrder.getUserId();
            String addressId = rentalOrder.getAddressId();
            CompletableFuture<RentalCarModels> rentalCarModelsFuture = CompletableFuture.supplyAsync(() -> rentalCarModelsService.queryById(modelId), executorService);
            CompletableFuture<RentalCars> rentalCarsFuture = CompletableFuture.supplyAsync(() -> rentalCarsService.queryById(carId), executorService);
            CompletableFuture<RentalCompany> rentalCompanyFuture = CompletableFuture.supplyAsync(() -> rentalCompanyService.queryById(companyId), executorService);
            CompletableFuture<RentalCarAddress> rentalCarAdressFuture = CompletableFuture.supplyAsync(() -> rentalCarAddressService.queryById(addressId), executorService);
            CompletableFuture<RentalUser> rentalUserFuture = CompletableFuture.supplyAsync(() -> rentalUserMapper.selectById(userId), executorService);
            CompletableFuture.allOf(rentalCarsFuture, rentalCompanyFuture, rentalCarModelsFuture, rentalCarAdressFuture).join();
            resRentalOrder.setRentalCars(rentalCarsFuture.get());
            resRentalOrder.setRentalCompany(rentalCompanyFuture.get());
            resRentalOrder.setRentalCarModels(rentalCarModelsFuture.get());
            resRentalOrder.setRentalOrder(rentalOrder);
            resRentalOrder.setRentalCarAddress(rentalCarAdressFuture.get());
            RentalUser rentalUser = rentalUserFuture.get();
            rentalUser.setUserPass(null);
            rentalUser.setPassSalt(null);
            resRentalOrder.setRentalUser(rentalUser);
            result.add(resRentalOrder);
        }

        IPage<ResRentalOrder> list = new Page();
        list.setRecords(result);
        list.setTotal(selectResult.getTotal());
        list.setCurrent(selectResult.getCurrent());
        list.setSize(selectResult.getSize());
        list.setPages(selectResult.getPages());
        list.setTotal(selectResult.getTotal());


        //3. 返回结果
        return list;
    }

    public List<ResRentalOrder> getOrderList() throws ExecutionException, InterruptedException {
        ExecutorService executorService = threadPoolUtils.getExecutorService();
        RentalUser userInfo = userUtils.getUserInfo();
        LambdaQueryWrapper<RentalOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentalOrder::getUserId, userInfo.getUserId());
        List<RentalOrder> rentalOrders = rentalOrderMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(RentalOrder::getCreatedTime))
                .collect(Collectors.toList());

        Collections.reverse(rentalOrders);
        List<ResRentalOrder> result = Lists.newArrayList();
        for (RentalOrder rentalOrder : rentalOrders) {

            if (rentalOrder.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT.getStatus())) {
                PayStatusRequest payStatusRequest = new PayStatusRequest();
                payStatusRequest.setOrderId(rentalOrder.getOtherOrderId());
                payStatusRequest.setBusinessId(rentalOrder.getOrderNo());
                PayStatusRespone payStatusRespone = this.queryOrderStatus(payStatusRequest);
                rentalOrder.setOrderStatus(payStatusRespone.getStatus());

            }


            ResRentalOrder resRentalOrder = new ResRentalOrder();
            String carId = rentalOrder.getCarId();
            String companyId = rentalOrder.getCompanyId();
            String modelId = rentalOrder.getModelId();
            String addressId = rentalOrder.getAddressId();
            Date createdTime = rentalOrder.getCreatedTime();
            Date date = new Date();
            if (rentalOrder.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT.getStatus()) && date.getTime() - createdTime.getTime() > 1000 * 60 * 15) {
                rentalOrder.setOrderStatus(OrderStatus.CANCELLED.getStatus());
                this.asyncUpdate(rentalOrder);
            }

            CompletableFuture<RentalCarModels> rentalCarModelsFuture = CompletableFuture.supplyAsync(() -> rentalCarModelsService.queryById(modelId), executorService);
            CompletableFuture<RentalCars> rentalCarsFuture = CompletableFuture.supplyAsync(() -> rentalCarsService.queryById(carId), executorService);
            CompletableFuture<RentalCompany> rentalCompanyFuture = CompletableFuture.supplyAsync(() -> rentalCompanyService.queryById(companyId), executorService);
            CompletableFuture<RentalCarAddress> rentalCarAdressFuture = CompletableFuture.supplyAsync(() -> rentalCarAddressService.queryById(addressId), executorService);
            CompletableFuture.allOf(rentalCarsFuture, rentalCompanyFuture, rentalCarModelsFuture, rentalCarAdressFuture).join();
            resRentalOrder.setRentalCars(rentalCarsFuture.get());
            resRentalOrder.setRentalCompany(rentalCompanyFuture.get());
            resRentalOrder.setRentalCarModels(rentalCarModelsFuture.get());
            resRentalOrder.setRentalOrder(rentalOrder);
            resRentalOrder.setRentalCarAddress(rentalCarAdressFuture.get());
            result.add(resRentalOrder);
        }
        return result;
    }

    @Async
    private void asyncUpdate(RentalOrder rentalOrder) {
        this.update(rentalOrder);
    }

    public Boolean updateOrderStatus(ReqRentalOrderStatus orderStatus) {

        String orderNo = orderStatus.getOrderId();
        LambdaQueryWrapper<RentalOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentalOrder::getOrderNo, orderNo);
        RentalOrder rentalOrders = rentalOrderMapper.selectOne(queryWrapper);

        RAssert.nonNull(rentalOrders, ErrorCode.NOT_FOUND_ORDER);
        rentalOrders.setOrderStatus(orderStatus.getOrderStatus());

        return rentalOrderMapper.updateById(rentalOrders) > 0;
    }
}