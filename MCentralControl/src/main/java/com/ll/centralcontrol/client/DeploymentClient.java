package com.ll.centralcontrol.client;

import com.ll.common.config.IpConfig;
import com.ll.common.utils.MResponse;
import com.ll.common.utils.MURIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */

@Component
public class DeploymentClient {


    @Autowired
    private RestTemplate restTemplate;


    public MResponse deployment(String name, String times){
        HttpHeaders headers = new HttpHeaders();
        headers.set("name", name);
        headers.set("times", times);
        restTemplate.exchange(
                MURIUtils.getRemoteUri(IpConfig.MDEPLOYMENT_IP, IpConfig.MDEPLOYMENT_PORT, "/deploy"),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                Void.class
        );
        return MResponse.successResponse();
    }
}
