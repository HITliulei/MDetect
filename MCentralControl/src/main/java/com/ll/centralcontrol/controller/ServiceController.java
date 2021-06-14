package com.ll.centralcontrol.controller;

import com.ll.centralcontrol.client.BuilderCenterClient;
import com.ll.centralcontrol.client.ServiceAnalysisClient;
import com.ll.centralcontrol.utils.MDatabaseUtils;
import com.ll.common.bean.MServiceRegisterBean;
import com.ll.common.bean.MSystemInfo;
import com.ll.common.service.MService;
import com.ll.common.service.MSvcInterface;
import com.ll.common.service.MSvcVersion;
import com.ll.common.utils.MIDUtils;
import com.ll.common.utils.MResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/24
 */

@RestController
@RequestMapping("/centerController")
public class ServiceController {

    private static Logger logger = LogManager.getLogger(ServiceController.class);

    private static Set<String> allServiceId  = new HashSet<String>();

    @Autowired
    private ServiceAnalysisClient serviceAnalysisClient;


    @Autowired
    private BuilderCenterClient builderCenterClient;


    @Autowired
    private MDatabaseUtils mDatabaseUtils;



    @PostMapping(value = "/registerOnservice")
    public MResponse registerService(@RequestBody MServiceRegisterBean registerBean) {
        return serviceAnalysisClient.getServiceInfo(registerBean);
    }


    // 对所有的微服务进行相应的检测
    @PostMapping(value = "/register")
    public MResponse registerService(@RequestBody MSystemInfo mSystemInfo) {
        return serviceAnalysisClient.getSystemInfo(mSystemInfo);
    }

    @PostMapping("/pushServiceInfo/{branch}")
    public MResponse pushServiceInfo(@PathVariable("branch")String branch, @RequestBody MResponse<List<MService>> mResponse){
        List<MService> serviceList = mResponse.getData();
        if (serviceList == null || serviceList.size() == 0){
            return MResponse.successResponse().code(1);
        }
        logger.info("获取所有的微服务信息");
        serviceList.stream().forEach(System.out::println);
        Iterator<MService> interator = serviceList.iterator();

        while (interator.hasNext()){
            MService a = interator.next();
            if (allServiceId.contains(a.getServiceId())){
                interator.remove();
            }else{
                allServiceId.add(a.getServiceId());
            }
        }
        // 信息存储
        // 构建项目
        builderCenterClient.complieProject(branch);
        logger.info("进行构建");
        Set<String> exitService = mDatabaseUtils.getALlServiceVersionInfo();
        for (MService service : serviceList) {
            for (MSvcInterface serviceInterface : service.getServiceInterfaceMap().values()) {
//                serviceInterface.setId(MIDUtils.uniqueInterfaceId(service.getServiceName(), serviceInterface.getFunctionName()));
                serviceInterface.setServiceId(service.getServiceId());
                serviceInterface.setInterfaceId(serviceInterface.getInterfaceId());
            }
            // 对于每一个serivce进行 docker 镜像的构建和存储
            if (exitService.contains(service.getServiceId())){
                continue;
            }else{
                // 镜像的构建以及 数据库的存储
                builderCenterClient.buildProject(branch, service.getServiceName(), service.getServiceVersion().toString());
                service.setImageUrl(service.getServiceName().toLowerCase() + ":" + service.getServiceVersion().toCommonStr());
                mDatabaseUtils.insertService(service);
                exitService.add(service.getServiceId());
            }
        }
        return MResponse.successResponse();
    }
}
