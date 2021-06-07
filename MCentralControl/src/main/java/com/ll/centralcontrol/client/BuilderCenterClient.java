package com.ll.centralcontrol.client;

import com.ll.common.bean.MBuildInfo;
import com.ll.common.bean.MServiceRegisterBean;
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
 * @date 2021/6/7
 */

@Component
public class BuilderCenterClient {

    @Autowired
    private RestTemplate restTemplate;

    // complie
    public MResponse complieProject(){
        return restTemplate.exchange(
                MURIUtils.getRemoteUri(IpConfig.MBUILD_CENTER_IP, IpConfig.MBUILD_CENTER_PORT, "/buildCenter/compile"),
                HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                MResponse.class).getBody();
    }


    //build

    public MResponse buildProject(String serviceName, String serviceVersion){

        return restTemplate.exchange(
                MURIUtils.getRemoteUri(IpConfig.MBUILD_CENTER_IP, IpConfig.MBUILD_CENTER_PORT, "/buildCenter/buildImage"),
                HttpMethod.POST,
                new HttpEntity<>(new MBuildInfo(serviceName, serviceVersion), new HttpHeaders()),
                MResponse.class).getBody();
    }

}
