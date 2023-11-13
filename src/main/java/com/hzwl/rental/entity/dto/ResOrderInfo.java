package com.hzwl.rental.entity.dto;

import com.hzwl.rental.entity.user.*;
import lombok.Data;

/**
 * @Author GA666666
 * @Date 2023/10/8 10:57
 */
@Data
public class ResOrderInfo {

    private RentalOrder rentalOrder;

    private RentalUser rentalUser;

    private RentalCompany rentalCompany;

    private RentalCoupons rentalCoupons;

    private RentalCarAddress rentalCarAddress;

    private RentalCars rentalCars;

    private RentalCarModels rentalCarModels;
}
