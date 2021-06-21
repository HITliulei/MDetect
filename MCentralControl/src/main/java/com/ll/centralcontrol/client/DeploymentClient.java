package com.ll.centralcontrol.client;

import com.ll.common.bean.EX.DeServiceInfo;
import com.ll.common.bean.EX.ServiceInfo;
import com.ll.common.config.IpConfig;
import com.ll.common.utils.MResponse;
import com.ll.common.utils.MURIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */

@Component
public class DeploymentClient {


    @Autowired
    private RestTemplate restTemplate;


    public MResponse deployment(String name, String times, DeServiceInfo deServiceInfo){
        HttpHeaders headers = new HttpHeaders();
        headers.set("name", name);
        headers.set("times", times);
        restTemplate.exchange(
                MURIUtils.getRemoteUri(IpConfig.MDEPLOYMENT_IP, IpConfig.MDEPLOYMENT_PORT, "/Deployment"),
                HttpMethod.GET,
                new HttpEntity<>(deServiceInfo, headers),
                Void.class
        );
        return MResponse.successResponse();
    }


    public MResponse deploymentCommand(HttpHeaders httpHeaders){
        restTemplate.exchange(
                MURIUtils.getRemoteUri(IpConfig.MDEPLOYMENT_IP, IpConfig.MDEPLOYMENT_PORT, "/deploycommand"),
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                Void.class
        );
        return MResponse.successResponse();
    }


    public static void main(String[] args) {
        ArrayList<ServiceInfo> a = new ArrayList<>();
        DeServiceInfo d = new DeServiceInfo();
        d.setServiceInfos(a);
        a.add(new ServiceInfo("service1", "1.3.1", 1990));
        a.add(new ServiceInfo("service2", "1.0.1", 190123));

        String name = "qe";
        String times = "11";
        HttpHeaders headers = new HttpHeaders();
        headers.set("name", name);
        headers.set("times", times);
        new RestTemplate().exchange(
                MURIUtils.getRemoteUri(IpConfig.MDEPLOYMENT_IP, IpConfig.MDEPLOYMENT_PORT, "/deploycommand"),
                HttpMethod.POST,
                new HttpEntity<>(d, headers),
                Void.class
        );
    }
}
