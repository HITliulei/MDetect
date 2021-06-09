package com.ll.common.bean.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/8
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MServiceInfo {
    private String serviceName;

    private Integer servicePort;

    private String serviceIp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MServiceInfo)) return false;
        MServiceInfo that = (MServiceInfo) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(servicePort, that.servicePort) && Objects.equals(serviceIp, that.serviceIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, servicePort, serviceIp);
    }
}
