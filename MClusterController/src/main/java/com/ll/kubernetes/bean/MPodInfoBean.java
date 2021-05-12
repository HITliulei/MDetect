package com.ll.kubernetes.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */

@Getter
@Setter
@ToString
public class MPodInfoBean {

    // pod name
    private String podName;

    // server ip
    private String serverIp;

    // the server node
    private String node;

    // the docker info
    private String dockerIp;

    // docker's pod ip
    private String podIp;
}
