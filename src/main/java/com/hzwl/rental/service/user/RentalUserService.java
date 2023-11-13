package com.hzwl.rental.service.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.Hutool;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hzwl.rental.config.VerificationCodeManager;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.constants.PayType;
import com.hzwl.rental.constants.UserStatus;
import com.hzwl.rental.entity.dto.HzwlUser;
import com.hzwl.rental.entity.dto.Location;
import com.hzwl.rental.entity.user.RentalUser;
import com.hzwl.rental.mapper.user.RentalUserMapper;
import com.hzwl.rental.utils.EnvironmentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author GA666666
 * @Date 2023/8/29 23:09
 */
@Service
public class RentalUserService {

    @Autowired
    private RentalUserMapper userMapper;

    @Value("${bianjietongxun.authentication.token}") // 从配置文件中获取认证令牌
    private String authenticationToken;

    @Value("${bianjietongxun.send-sim-url}") // 从配置文件中获取认证令牌
    private String sendSimUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VerificationCodeManager verificationCodeManager;

    @Autowired
    private HzwlHttpService hzwlHttpService;

    @Value("${bianjietongxun.query-user-by-key}")
    private String queryUserUrl;


    public HzwlUser getHzwlUserByKey(String key){
        Map<String, Object> body = new HashMap<>();
        body.put("key", key);
        String responseJson = hzwlHttpService.sendHttpRequest(this.queryUserUrl, HttpMethod.POST, body);
        JSONObject jsonObject = JSONObject.parseObject(responseJson);
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            return jsonObject.getObject("data", HzwlUser.class);
        } else {
            throw new BizException(ErrorCode.QUERY_LOCATION_BY_IP_ERROR);
        }
    }

    public RentalUser login(String phoneNumber) {
        QueryWrapper<RentalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phoneNumber)
                .eq("username", phoneNumber);
        RentalUser rentalUser = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(rentalUser)) {
            rentalUser = this.registerUserByPhoneNumber(phoneNumber);
        }

        this.updateLastLoginTime(rentalUser);
        StpUtil.login(rentalUser.getUserId());
        return rentalUser;
    }


    /**
     * 注册新用户
     *
     * @param phoneNumber 用户手机号
     * @return 是否成功
     */
    private RentalUser registerUserByPhoneNumber(String phoneNumber) {

        // 创建一个新的 RentalUser 对象
        RentalUser user = new RentalUser();
        user.setPhone(phoneNumber);
        user.setUsername(phoneNumber);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        user.setUserStatus(UserStatus.NORMAL.getStatus());

        if (userMapper.insert(user) > 0) {
            return user;
        }
        // 使用 RentalUserMapper 进行注册操作
        return null;
    }

    public String simCode(String phone){
        verificationCodeManager.storeCode(phone,"123456");
        return "asda";

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("authentication", this.authenticationToken);
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("uid", RandomUtil.randomNumbers(8));
//        body.put("phone",phone);
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                this.sendSimUrl,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
//            assert jsonObject != null;
//            if (jsonObject.getInteger("code") == 200) {
//                String data = jsonObject.getString("data");
//                smsCodeCache.put(phone, data); // 存储验证码到缓存
//                return data;
//            }
//            throw new BizException(ErrorCode.SMS_CODE_SEND_ERROR);
//        }
//        throw new BizException(ErrorCode.SMS_CODE_SEND_ERROR);
    }

    /**
     * 验证缓存验证码
     * @param phoneNumber
     * @param inputCode
     * @return
     */
    public boolean verifySmsCode(String phoneNumber, String inputCode) {
        return verificationCodeManager.verifyCode(phoneNumber,inputCode); // 比较输入的验证码和缓存中的验证码
    }


    @Async
    private void updateLastLoginTime(RentalUser rentalUser) {
        rentalUser.setLastLoginTime(new Date());
        userMapper.updateById(rentalUser);
    }




}
