package com.ll.common.trace;

import com.alibaba.fastjson.JSONObject;
import com.ll.common.utils.MJSONUtils;
import lombok.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/3
 */


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Trace {

    // 实验标识
    private String ex;

    // 唯一标识
    private String tracId;


    // 当前微服务的ip
    private String localIp;

    // 当前微服的名称
    private String serviceId;

    // 请求的是哪一个路径
    private String requestPath;

    // 请求的是哪一个接口：
    private String requestInterface;

    // 请求来源的ip请求
    private String sourceIp;

    // 请求的开始时间
    private String startTime;

    // 请求的结束时间
    private String endTime;

    // 请求的方式
    private String method;

    // 是否成功
    private String success;


    @Override
    public String toString(){
        return MJSONUtils.toJSONString(this);
    }

}
