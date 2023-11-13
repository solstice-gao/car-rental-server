package com.hzwl.rental.entity.dto;

import com.hzwl.rental.entity.user.RentalCarAddress;
import com.hzwl.rental.entity.user.RentalCarModels;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author GA666666
 * @Date 2023/10/6 23:26
 */
@Data
public class ReqColorModel {
    private String carSendMarktext;
    private BigDecimal price;
    private String carDesc;
    private String carSendFrom;
    private String companyId;
    private String carDescTitle;
    private List<String> colors;
    private List<String> models;
    private List<RentalCarModels> prices;
    private List<RentalCarAddress> adress;
}
