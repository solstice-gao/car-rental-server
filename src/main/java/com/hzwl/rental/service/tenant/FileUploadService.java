package com.hzwl.rental.service.tenant;

import com.alibaba.fastjson.JSONObject;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {

    @Value("${file.upload.url}") // 从配置文件中获取上传文件的URL
    private String fileUploadUrl;

    @Value("${bianjietongxun.authentication.token}") // 从配置文件中获取认证令牌
    private String authenticationToken;

    @Autowired
    private RestTemplate restTemplate;

    public String uploadFile(MultipartFile file) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("authentication", this.authenticationToken);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                this.fileUploadUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            assert jsonObject != null;
            if (jsonObject.getInteger("code") == 200) {
                return jsonObject.getString("data");
            }
            throw new BizException(ErrorCode.IMAGE_UPLOAD_ERROR);
        }
        throw new BizException(ErrorCode.IMAGE_UPLOAD_ERROR);
    }

}
