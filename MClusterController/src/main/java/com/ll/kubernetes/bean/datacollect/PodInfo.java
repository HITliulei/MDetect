package com.ll.kubernetes.bean.datacollect;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/29
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PodInfo {

    // pod name
    private String podName;

    private String podIp;

    private String node;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
//        return "{" +
//                "'podName':'" + podName + '\'' +
//                ", 'podIp':'" + podIp + '\'' +
//                ", 'node':'" + node + '\'' +
//                '}';
    }
}
