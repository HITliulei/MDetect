package com.ll.common.bean.system;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/8
 */

@Getter
public class MServiceInfo {
    private final String serviceName;

    private final Integer servicePort;

    private final String serviceIp;

    public MServiceInfo(String serviceName, Integer servicePort, String serviceIp) {
        this.serviceName = serviceName;
        this.servicePort = servicePort;
        this.serviceIp = serviceIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MServiceInfo)) return false;
        MServiceInfo that = (MServiceInfo) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(servicePort, that.servicePort) && Objects.equals(serviceIp, that.serviceIp);
    }

}
