package com.ll.common.service;

import com.alibaba.fastjson.JSONObject;
import com.ll.common.base.MUniqueObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/13
 */
@Getter
@Setter
@ToString
public class MService extends MUniqueObject {
    /*
     * The service name is not the same as the serviceId.
     * For two services, they can have the same service name with different version.
     */


    private String serviceId;  //  "由serviceName_serviceVersion组成"
    private String serviceName;
    private MSvcVersion serviceVersion;
    private int port;
    private String imageUrl;
    private String basePath;
    private Map<String, MSvcInterface> serviceInterfaceMap;


    public Optional<MSvcInterface> getInterfaceByPatternUrl(String patternUrl) {
        for (MSvcInterface svcInterface : this.serviceInterfaceMap.values()) {
            if (svcInterface.getPatternUrl().equals(patternUrl)) {
                return Optional.of(svcInterface);
            }
        }
        return Optional.empty();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MService service = (MService) o;

        if (this.serviceInterfaceMap.size() != service.serviceInterfaceMap.size()) {
            return false;
        }
        for (String interfaceId : this.serviceInterfaceMap.keySet()) {
            if (!this.serviceInterfaceMap.get(interfaceId).equals(service.serviceInterfaceMap.get(interfaceId))) {
                return false;
            }
        }

        return port == service.port &&
                Objects.equals(serviceName, service.serviceName) &&
                Objects.equals(serviceVersion, service.serviceVersion) &&
                Objects.equals(imageUrl, service.imageUrl);
    }

    public MSvcInterface getInterfaceById(String interfaceId) {
        return this.getServiceInterfaceMap().get(interfaceId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(serviceId);
    }


    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
