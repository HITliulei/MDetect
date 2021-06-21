package com.ll.common.bean.EX;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/19
 */
@Getter
@Setter
public class ServiceInfo{

    private String serviceName;
    private String serviceVersion;
    private Integer servicePort;


    public ServiceInfo(){

    }

    public ServiceInfo(String serviceName, String serviceVersion, Integer servicePort){
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.servicePort = servicePort;
    }

}
