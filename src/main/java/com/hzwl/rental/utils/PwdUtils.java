package com.hzwl.rental.utils;

import cn.hutool.crypto.SecureUtil;
import org.springframework.stereotype.Component;

/**
 * @Author GA666666
 * @Date 2023/9/3 20:18
 */
@Component
public class PwdUtils {

    public static String encodePassword(String password) {
        String encode = SecureUtil.md5(password + "2023");
        return encode;
    }
}
