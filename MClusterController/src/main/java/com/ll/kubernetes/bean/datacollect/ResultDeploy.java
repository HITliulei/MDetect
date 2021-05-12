package com.ll.kubernetes.bean.datacollect;

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

    private String serviceName;

    private Integer port;

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
