package com.hzwl.rental.service.user;

import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author GA666666
 * @Date 2023/9/14 17:08
 */
@Service
public class HzwlHttpService {

    @Value("${bianjietongxun.authentication.token}") // 从配置文件中获取认证令牌
    private String authenticationToken;

    @Autowired
    private RestTemplate restTemplate;

    public String sendHttpRequest(String url, HttpMethod method, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authentication", this.authenticationToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                method,
                requestEntity,
                String.class  // 设置响应类型为 String
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new BizException(ErrorCode.CREATE_PRE_ORDER_ERROR);
        }
    }
}
