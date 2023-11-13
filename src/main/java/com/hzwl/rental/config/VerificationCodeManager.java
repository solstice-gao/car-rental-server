package com.hzwl.rental.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeManager {

    private Cache<String, String> codeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();

    // 生成随机验证码并存储
    public String storeCode(String phoneNumber,String code) {
        codeCache.put(phoneNumber, code);
        return code;
    }


    // 验证验证码是否正确
    public boolean verifyCode(String phoneNumber, String code) {
        String storedCode = codeCache.getIfPresent(phoneNumber);
        return storedCode != null && storedCode.equals(code);
    }
}
