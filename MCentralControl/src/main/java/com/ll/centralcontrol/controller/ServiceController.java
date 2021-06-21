package com.ll.centralcontrol.controller;

import com.ll.centralcontrol.client.ServiceAnalysisClient;
import com.ll.centralcontrol.service.ServiceAnalysis;
import com.ll.common.bean.MServiceRegisterBean;
import com.ll.common.bean.MSystemInfo;
import com.ll.common.service.MService;
import com.ll.common.utils.MResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/24
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/serviceController")
public class ServiceController {

    private static Logger logger = LogManager.getLogger(ServiceController.class);

    @Autowired
    private ServiceAnalysisClient serviceAnalysisClient;


    @Autowired
    @Qualifier("serviceAnalysisImpl")
    private ServiceAnalysis serviceAnalysis;



    @PostMapping(value = "/registerOnservice")
    public MResponse registerService(@RequestBody MServiceRegisterBean registerBean) {
        return serviceAnalysisClient.getServiceInfo(registerBean);
    }

    /**
     * 注册一个系统
     * @param mSystemInfo
     * @return
     */
    // 对所有的微服务进行相应的检测
    @PostMapping(value = "/register")
    public MResponse registerService(@RequestBody MSystemInfo mSystemInfo) {
        return serviceAnalysisClient.getSystemInfo(mSystemInfo);
    }

    /**
     * 服务信息构建以及入库。
     * @param branch 分析的分支
     * @param mResponse 接收回来的MService
     * @return
     */
    @PostMapping("/pushServiceInfo/{branch}")
    public MResponse pushServiceInfo(@PathVariable("branch")String branch, @RequestBody MResponse<List<MService>> mResponse){
        List<MService> serviceList = mResponse.getData();
        if (serviceList == null || serviceList.size() == 0){
            return MResponse.failedResponse().code(0);
        }
        try {
            serviceAnalysis.pushInfo(serviceList, branch);
        }catch (Exception e){
            e.printStackTrace();
            return MResponse.failedResponse().code(0).status("exception");
        }
        return MResponse.successResponse().code(1);
    }
}
