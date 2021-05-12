package com.ll.kubernetes.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/3
 */
@Getter
@Setter
@ToString
public class MDockerInfoBean {

    // server ip
   private String hostIp;

   // the server node
    private String nodeLabel;

    // the docker info
    private String instanceId;

    // docker's pod ip
    private String podIp;

    //
    private String serviceName;

}
