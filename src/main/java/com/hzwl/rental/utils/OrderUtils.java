package com.hzwl.rental.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @Author GA666666
 * @Date 2023/9/9 23:40
 */
public class OrderUtils {

    public static String generateOrderNumber() {
        // 使用雪花算法生成唯一的ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long orderId = snowflake.nextId();

        return orderId+"";
    }
}
