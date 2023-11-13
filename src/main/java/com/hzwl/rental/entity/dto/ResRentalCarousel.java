package com.hzwl.rental.entity.dto;

import com.hzwl.rental.entity.user.RentalCarousel;
import lombok.Data;

import java.util.List;

@Data
public class ResRentalCarousel {

    private String city;


    private List<RentalCarousel> rentalCarousels;

}
