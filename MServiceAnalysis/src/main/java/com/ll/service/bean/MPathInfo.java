package com.ll.service.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Lei on 2019/11/29 15:53
 */

@Getter
@Setter
public class MPathInfo {

    private String serviceName;
    private String applicationPath;
    private List<String> controllerListPath;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
