package com.hzwl.rental.service.user;

import com.alibaba.fastjson.JSONObject;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.Location;
import com.hzwl.rental.entity.dto.PayResponeData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author GA666666
 * @Date 2023/9/14 17:06
 */
@Service
public class IPService {
    @Resource
    private HzwlHttpService hzwlHttpService;

    @Value("${bianjietongxun.query-location-url}")
    private String queryLocationUrl;

    /**
     * 获取信息
     *
     * @param ip
     * @return
     */
    public Location getLocation(String ip) {
        String responseJson = hzwlHttpService.sendHttpRequest(this.queryLocationUrl + ip, HttpMethod.GET, null);

        JSONObject jsonObject = JSONObject.parseObject(responseJson);

        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            return jsonObject.getObject("data", Location.class);
        } else {
            throw new BizException(ErrorCode.QUERY_LOCATION_BY_IP_ERROR);
        }
    }


}
