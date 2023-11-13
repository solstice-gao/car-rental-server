package com.hzwl.rental.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.hzwl.rental.entity.user.RentalUser;
import com.hzwl.rental.mapper.user.RentalUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author GA666666
 * @Date 2023/8/30 23:16
 */

@Component
public class UserUtils {

    @Autowired
    private RentalUserMapper userMapper;


    public static String getUserId() {
        String userId = (String) StpUtil.getLoginId();
        return userId;
    }

    public RentalUser getUserInfo() {
        String userId = getUserId();
        RentalUser rentalUser = userMapper.selectById(userId);
        return rentalUser;
    }
}
