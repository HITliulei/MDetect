package com.ll.common.bean.deployInfoCollect;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/3
 */

@Getter
@Setter
public class ResultDeploy {

    // 服务名称
    private String serviceName;

    // 服务的版本
    private String serviceVersion;

    //服务的端口号
    private Integer port;

    // 服务的部署结构
    private Map<String, List<PodInfo>> map;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
//        return "{" +
//                "'serviceName':" + serviceName  +
//                ", 'port':" + port +
//                ", 'map':" + map.toString().replaceAll("=", ":") +
//                '}';
    }
}
