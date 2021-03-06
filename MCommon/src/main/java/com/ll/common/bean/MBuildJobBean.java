package com.ll.common.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/1/6
 *
 * This class is used when server communicates with the build center for building a maven based project
 */
@Getter
@Setter
@ToString
public class MBuildJobBean {
    private String serviceName;
    private String gitUrl;
    private String serviceVersion;

}
