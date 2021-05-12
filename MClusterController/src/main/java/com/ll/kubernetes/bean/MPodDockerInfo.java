package com.ll.kubernetes.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/17
 */

@Getter
@Setter
@ToString
public class MPodDockerInfo {

    private MDockerInfoBean mDockerInfoBean;

    private String allDockerId;

    private String dockerId;

    private String node;

    // docker 对应的进程id
    private String PID;

}
