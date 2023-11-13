package com.hzwl.rental.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author GA666666
 * @Date 2023/9/10 00:21
 */
@Component
public class EnvironmentUtils {

    @Value("${spring.profiles.active}")
    private String env;

    public boolean isDev(){
        return env.equals("dev");
    }

    public boolean isProd(){
        return !env.equals("dev");
    }
}
