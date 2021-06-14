package com.ll.centralcontrol.dao;

import com.ll.common.service.MService;
import com.ll.common.service.MSvcVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/20
 */
@Getter
@Setter
@ToString
public class MServiceDao {
    private String serviceId;
    private String serviceName;
    private String serviceVersion;
    private String serviceImage;
    private Integer port;
    private String basePath;

    public MServiceDao() { }

    public MServiceDao(String serviceId, String serviceName, String serviceVersion, String serviceImage, Integer port, String basePath) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceImage = serviceImage;
        this.port = port;
        this.basePath = basePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MServiceDao that = (MServiceDao) o;
        return Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(serviceVersion, that.serviceVersion) &&
                Objects.equals(serviceImage, that.serviceImage) &&
                Objects.equals(port, that.port) &&
                Objects.equals(basePath, that.basePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, serviceName, serviceVersion, serviceImage, port, basePath);
    }

    public static MServiceDao fromDto(MService service) {
        return new MServiceDao(
                service.getServiceId(),
                service.getServiceName(),
                service.getServiceVersion().toString(),
                service.getImageUrl(),
                service.getPort(),
                service.getBasePath()
        );
    }

    public MService toDto() {
        MService service = new MService();
        service.setId(this.serviceId);
        service.setServiceName(this.serviceName);
        service.setServiceVersion(MSvcVersion.fromStr(this.serviceVersion));
        service.setImageUrl(this.serviceImage);
        service.setPort(this.port);
        service.setBasePath(this.basePath);
        return service;
    }
}
