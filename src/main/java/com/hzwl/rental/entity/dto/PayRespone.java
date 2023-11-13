package com.hzwl.rental.entity.dto;

import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/9/10 01:51
 */
@Data
public class PayRespone<T> {

    private Integer code;

    private String msg;

    private T data;

}
