package com.hzwl.rental.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hzwl.rental.entity.user.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author GA666666
 * @Date 2023/9/11 16:01
 */
@Data
public class ResRentalOrder {

    private RentalOrder rentalOrder;

    private RentalCompany rentalCompany;

    private RentalCars rentalCars;

    private RentalCarModels rentalCarModels;

    private RentalCarAddress rentalCarAddress;

    private RentalUser rentalUser;

}
