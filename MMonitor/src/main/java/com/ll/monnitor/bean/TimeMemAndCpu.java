package com.ll.monnitor.bean;
import com.alibaba.fastjson.JSONObject;
import com.ll.monnitor.metrics.MetricsUsage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */

@Getter
@Setter
public class TimeMemAndCpu {

    private Date date;

    private Map<String, MetricsUsage> map;

    public TimeMemAndCpu(){
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
