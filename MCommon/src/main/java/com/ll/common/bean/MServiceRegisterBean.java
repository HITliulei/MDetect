package com.ll.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/25
 */
@Getter
@Setter
public class MServiceRegisterBean {
    private String serviceName;
    private String gitUrl;
    private String branch;
}

