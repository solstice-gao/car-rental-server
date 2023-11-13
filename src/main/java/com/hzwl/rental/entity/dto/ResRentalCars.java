package com.hzwl.rental.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hzwl.rental.entity.user.RentalCarModels;
import com.hzwl.rental.entity.user.RentalCompany;
import com.hzwl.rental.entity.user.RentalDeliveryAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author GA666666
 * @Date 2023/8/30 13:51
 */
@Data
public class ResRentalCars {

    @ApiModelProperty(name = "车辆ID", notes = "")
    @TableId
    private String carId;

    @ApiModelProperty(name = "车辆描述", notes = "")
    private String carDesc;

    @ApiModelProperty(name = "型号ID", notes = "")
    private String modelId;

    @ApiModelProperty(name = "门店ID", notes = "")
    private String storeId;

    @ApiModelProperty(name = "车辆价格", notes = "")
    private Double price;

    @ApiModelProperty(name = "描述标题", notes = "")
    private String carDescTitle;

    @ApiModelProperty(name = "车辆发货地", notes = "")
    private String carSendFrom;

    @ApiModelProperty(name = "备注", notes = "")
    private String carSendMarktext;

    @ApiModelProperty(name = "车辆效果图", notes = "")
    private String imageUrl;

    @ApiModelProperty(name = "头图", notes = "")
    private List<String> carImages;

    @ApiModelProperty(name = "详情图", notes = "")
    private List<String> carDescImages;

    @ApiModelProperty(name = "价格区间", notes = "")
    private String rangePrice;

    @ApiModelProperty(name = "可选型号", notes = "")
    private List<RentalCarModels> carModels;

    @ApiModelProperty(name = "提货地址", notes = "")
    private List<RentalDeliveryAddress> deliveryAddresses;

    @ApiModelProperty(name = "公司信息", notes = "")
    private RentalCompany  companyInfo;

}
