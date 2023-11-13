package com.hzwl.rental.entity.dto;

import com.hzwl.rental.entity.user.RentalCarModels;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author GA666666
 * @Date 2023/9/4 09:13
 */
@Data
public class ReqRentalCarModels {

    @NotNull
    @ApiModelProperty(name = "车辆ID", notes = "")
    private String carId;

    @Valid
    @NotNull
    @ApiModelProperty(name = "型号列表", notes = "")
    private List<RentalCarModels> modelsList;

}
