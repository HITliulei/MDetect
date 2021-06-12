package com.ll.service.client;

import com.ll.common.config.IpConfig;
import com.ll.common.service.MService;
import com.ll.common.utils.MResponse;
import com.ll.common.utils.MURIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */


@Component
public class ServerClient {


    @Autowired
    private RestTemplate restTemplate;

    public void pushServiceInfos(List<MService> list, String branch){
        System.out.println("发送信息");
        restTemplate.exchange(
                MURIUtils.getRemoteUri(IpConfig.CENTER_IP, IpConfig.CENTER_PORT, "/centerController/pushServiceInfo/" + branch),
                HttpMethod.POST,
                new HttpEntity<>(new MResponse<List<MService>>().data(list).code(1), new HttpHeaders()),
                MResponse.class);
    }
}
