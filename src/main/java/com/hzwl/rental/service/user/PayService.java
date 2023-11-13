package com.hzwl.rental.service.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.constants.PayType;
import com.hzwl.rental.entity.dto.PayRespone;
import com.hzwl.rental.entity.dto.PayResponeData;
import com.hzwl.rental.entity.dto.PayStatusRequest;
import com.hzwl.rental.entity.dto.PayStatusRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author GA666666
 * @Date 2023/9/9 23:33
 */
@Service
public class PayService {

    @Value("${bianjietongxun.notifyPayUrl}")
    private String notifyPayUrl;

    @Value("${bianjietongxun.pay-url}")
    private String payUrl;
    @Value("${bianjietongxun.query-pay-status-url}")
    private String payStatusUrl;


    @Resource
    private HzwlHttpService hzwlHttpService;


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public PayResponeData preAliPay(String businessId, BigDecimal amount, String description) {
        Map<String, Object> body = new HashMap<>();
        body.put("payChannel", PayType.ALIPAY.getCode());
        body.put("businessId", businessId);
        body.put("amount", amount.longValue());
        body.put("description", description);
        body.put("notifyUrl", notifyPayUrl);

        String responseJson = hzwlHttpService.sendHttpRequest(this.payUrl, HttpMethod.POST, body);

        JSONObject jsonObject = JSONObject.parseObject(responseJson);

        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            return jsonObject.getObject("data", PayResponeData.class);
        } else {
            throw new BizException(ErrorCode.CREATE_PRE_ORDER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public PayStatusRespone queryOrderStatus(PayStatusRequest payStatusRequest) {
        Map<String, Object> body = new HashMap<>();
        if (StringUtils.isNotBlank(payStatusRequest.getBusinessId())) {
            body.put("businessId", payStatusRequest.getBusinessId());
        }
        if (StringUtils.isNotBlank(payStatusRequest.getBusinessId())) {
            body.put("orderId", payStatusRequest.getOrderId());
        }


        String responseJson = hzwlHttpService.sendHttpRequest(this.payStatusUrl, HttpMethod.POST, body);

        JSONObject jsonObject = JSONObject.parseObject(responseJson);

        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            return jsonObject.getObject("data", PayStatusRespone.class);
        } else {
            throw new BizException(ErrorCode.CREATE_PRE_ORDER_ERROR);
        }
    }
}
