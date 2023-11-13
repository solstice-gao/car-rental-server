package com.hzwl.rental.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author GA666666
 * @Date 2023/10/7 02:17
 */
@Data
public class ReqUploadFile {
    private String type;
    private String carId;
    private MultipartFile file;
}
