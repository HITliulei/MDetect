package com.ll.centralcontrol.controller;

import com.ll.centralcontrol.client.ServiceAnalysisClient;
import com.ll.common.bean.MServiceRegisterBean;
import com.ll.common.bean.MSystemInfo;
import com.ll.common.service.MService;
import com.ll.common.service.MSvcInterface;
import com.ll.common.utils.MIDUtils;
import com.ll.common.utils.MResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/24
 */

@RestController
@RequestMapping("/centerController")
public class ServiceController {


    private static Set<String> allServiceId  = new HashSet<>();


    @PostMapping(value = "/registerOnservice")
    public MResponse registerService(@RequestBody MServiceRegisterBean registerBean) {
        return ServiceAnalysisClient.getServiceInfo(registerBean);
    }


    @PostMapping(value = "/register")
    public MResponse registerService(@RequestBody MSystemInfo mSystemInfo) {
        return ServiceAnalysisClient.getSystemInfo(mSystemInfo);
    }




    @PostMapping("/pushServiceInfo")
    public MResponse pushServiceInfo(@RequestBody MResponse<List<MService>> mResponse){
        List<MService> serviceList = mResponse.getData();
        if (serviceList == null || serviceList.size() == 0){
            return MResponse.successResponse();
        }
        Iterator<MService> interator = serviceList.iterator();

        while (interator.hasNext()){
            MService a = interator.next();
            if (allServiceId.contains(a.getServiceId())){
                interator.remove();
            }else{
                allServiceId.add(a.getServiceId());
            }
        }


        for (MService service : serviceList) {
            for (MSvcInterface serviceInterface : service.getServiceInterfaceMap().values()) {
                serviceInterface.setId(MIDUtils.uniqueInterfaceId(service.getServiceName(), serviceInterface.getFunctionName()));
                serviceInterface.setServiceId(service.getId());
            }
            service.setImageUrl("");
        }
        // 存储数据库







    }



}
