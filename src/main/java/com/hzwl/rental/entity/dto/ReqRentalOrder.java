package com.hzwl.rental.entity.dto;

import com.hzwl.rental.entity.user.RentalOrder;
import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/10/7 10:57
 */
@Data
public class ReqRentalOrder {
    private RentalOrder rentalOrder;

    private Integer page;

    private Integer pageSize;
}
