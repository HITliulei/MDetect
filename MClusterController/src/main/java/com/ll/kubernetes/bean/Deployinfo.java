package com.ll.kubernetes.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/10
 */

@Setter
@Getter
@ToString
public class Deployinfo {

    // service Name to deploy
    private String serviceName;

    private Integer port;

    // String: worker name
    // Integer: instance number
    private Map<String, Integer> map;

}
