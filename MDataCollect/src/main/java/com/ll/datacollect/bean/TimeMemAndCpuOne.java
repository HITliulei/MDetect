package com.ll.datacollect.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/1/1
 */

@Getter
@Setter
public class TimeMemAndCpuOne {

    private Date date;

    private MetricsUsage metricsUsage;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
