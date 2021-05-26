package com.ll.service.controller;

import com.ll.common.bean.MServiceRegisterBean;
import com.ll.common.bean.MSystemInfo;
import com.ll.common.service.MService;
import com.ll.common.utils.MResponse;
import com.ll.service.bean.MPathInfo;
import com.ll.service.client.ServerClient;
import com.ll.service.utils.GetServiceInfo;
import com.ll.service.utils.GetSystemCodeAndPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/25
 */

@RestController

@RequestMapping("/serviceAnalysis")
public class MOperation {


    private static Logger logger = LogManager.getLogger(MOperation.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);


    @Autowired
    private ServerClient serverClient;
    // 针对复旦的系统进行开发的相应接口
    @PostMapping("/getFudanInfoOfOneService")
    public MResponse getFudanInfoOfOneService(@RequestBody MServiceRegisterBean mServiceRegisterBean){
        String serviceName = mServiceRegisterBean.getServiceName();
        executorService.submit(() -> {
            logger.info ("处理复旦的系统的 + "+ serviceName +" 服务");
            MPathInfo mPathInfo = GetSystemCodeAndPath.getcodeByserviceName(
                    mServiceRegisterBean.getGitUrl(), mServiceRegisterBean.getBranch(),mServiceRegisterBean.getServiceName());
            MService mService = GetServiceInfo.getMservice(mPathInfo);
            List<MService> list = new ArrayList<MService>(){{add(mService);}};
            MResponse response = serverClient.pushServiceInfos(list);
            logger.info(String.format("Receive %s from server", response.getStatus()));
        });
        return MResponse.successResponse();
    }


    @PostMapping("/getFudanInfo")
    public MResponse getFudanInfo(@RequestBody MSystemInfo mSystemInfo){
        executorService.submit(() -> {
            // 获取了所有的服务信息
            Map<String, MPathInfo> map = GetSystemCodeAndPath.getcode(mSystemInfo.getGitUrl(), mSystemInfo.getBranch());
            List<MService> allMService =
                    map.values().stream().map(mPathInfo-> GetServiceInfo.getMservice(mPathInfo)).collect(Collectors.toList());
            MResponse response = serverClient.pushServiceInfos(allMService);
            logger.info(String.format("Receive %s from server", response.getStatus()));
        });
        return MResponse.successResponse();
    }



}