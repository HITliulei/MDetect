package com.ll.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/6
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MBuildInfo {

    // 服务的名字
    private String serviceName;

    // 服务的版本
    private String serviceVersion;
}