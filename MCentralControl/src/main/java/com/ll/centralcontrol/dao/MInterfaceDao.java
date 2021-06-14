package com.ll.centralcontrol.dao;

import com.ll.common.service.MSvcInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/28
 */
@Getter
@Setter
@ToString
public class MInterfaceDao {
    private String id;
    private String patternUrl;
    private String requestMethod;
    private String returnType;
    private String serviceId;
    private String functionName;

    public MInterfaceDao(String id, String patternUrl, String functionName, String requestMethod, String returnType, String serviceId) {
        this.id = id;
        this.functionName = functionName;
        this.patternUrl = patternUrl;
        this.requestMethod = requestMethod;
        this.returnType = returnType;
        this.serviceId = serviceId;
    }

    public MInterfaceDao() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MInterfaceDao that = (MInterfaceDao) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(patternUrl, that.patternUrl) &&
                Objects.equals(requestMethod, that.requestMethod) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(functionName, that.functionName);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patternUrl, requestMethod, returnType, serviceId, functionName);
    }

    public static MInterfaceDao fromDto(MSvcInterface serviceInterface) {
        String featureName = null;
        Integer slaLevel = null;
        return new MInterfaceDao(
            serviceInterface.getInterfaceId(),
            serviceInterface.getPatternUrl(),
            serviceInterface.getFunctionName(),
            serviceInterface.getRequestMethod(),
            serviceInterface.getReturnType(),
            serviceInterface.getServiceId()
        );
    }

    public MSvcInterface toDto() {
        MSvcInterface serviceInterface = new MSvcInterface();
        serviceInterface.setId(this.id);
        serviceInterface.setPatternUrl(this.patternUrl);
        serviceInterface.setFunctionName(this.functionName);
        serviceInterface.setRequestMethod(this.requestMethod);
        serviceInterface.setReturnType(this.returnType);
        serviceInterface.setServiceId(this.serviceId);
        return serviceInterface;
    }
}
