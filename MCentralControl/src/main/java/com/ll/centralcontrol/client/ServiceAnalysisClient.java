package com.ll.centralcontrol.client;

import com.ll.common.bean.MServiceRegisterBean;
import com.ll.common.bean.MSystemInfo;
import com.ll.common.config.IpConfig;
import com.ll.common.service.MService;
import com.ll.common.utils.MResponse;
import com.ll.common.utils.MURIUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */
public class ServiceAnalysisClient {



    public static MResponse getServiceInfo(MServiceRegisterBean mServiceRegisterBean){
        return new RestTemplate().exchange(
                MURIUtils.getRemoteUri(IpConfig.SERVICE_ANALSIS_IP, IpConfig.SERVICE_ANALSIS_PORT, "/serviceAnalysis/getFudanInfoOfOneService"),
                HttpMethod.POST,
                new HttpEntity<>(mServiceRegisterBean, new HttpHeaders()),
                MResponse.class).getBody();
    }


    public static MResponse getSystemInfo(MSystemInfo mSystemInfo){
        return new RestTemplate().exchange(
                MURIUtils.getRemoteUri(IpConfig.SERVICE_ANALSIS_IP, IpConfig.SERVICE_ANALSIS_PORT, "/serviceAnalysis/getFudanInfo"),
                HttpMethod.POST,
                new HttpEntity<>(mSystemInfo, new HttpHeaders()),
                MResponse.class).getBody();
    }
}
