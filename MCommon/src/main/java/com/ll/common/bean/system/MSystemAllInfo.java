package com.ll.common.bean.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ll.common.config.ServiceConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/8
 */

@Getter
@Setter
public class MSystemAllInfo {


    private List<MServiceInfo> list;


    public MSystemAllInfo(){
        JSONObject jsonObject = JSON.parseObject(ServiceConfig.SYSTEM_SERVICE_INFO);
        for (String serviceName : jsonObject.keySet()){
            list.add(new MServiceInfo(serviceName, jsonObject.getInteger(serviceName), "v1.0.0"));
        }
    }


    public boolean add(MServiceInfo mServiceInfo){
        return this.list.add(mServiceInfo);
    }
}
