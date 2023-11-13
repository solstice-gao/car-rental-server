package com.hzwl.rental.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author GA666666
 * @Date 2023/9/14 17:12
 */
@Data
public class Location {
    private String ip;
    private String nation;
    private String province;
    private String city;
    private String district;
    private double longitude;
    private double latitude;
    private Date time;
}
