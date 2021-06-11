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
import org.springframework.beans.factory.annotation.Qualifier;
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
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private ServerClient serverClient;
    // 针对复旦的系统进行开发的相应接口
    @PostMapping("/getFudanInfoOfOneService")
    public MResponse getFudanInfoOfOneService(@RequestBody MServiceRegisterBean mServiceRegisterBean){
        String serviceName = mServiceRegisterBean.getServiceName();
        executorService.submit(() -> {
            try {
                logger.info ("处理复旦的系统的 + "+ serviceName +" 服务");
                MPathInfo mPathInfo = GetSystemCodeAndPath.getcodeByserviceName(
                        mServiceRegisterBean.getGitUrl(), mServiceRegisterBean.getBranch(),mServiceRegisterBean.getServiceName());
                MService mService = GetServiceInfo.getMservice(mPathInfo);
                List<MService> list = new ArrayList<MService>(){{add(mService);}};
                serverClient.pushServiceInfos(list, mServiceRegisterBean.getBranch());
            }catch (Exception e){
                e.printStackTrace();
                serverClient.pushServiceInfos(new ArrayList<>(), mServiceRegisterBean.getBranch());
            }


        });
        return MResponse.successResponse().code(1);
    }


    @PostMapping("/getFudanInfo")
    public MResponse getFudanInfo(@RequestBody MSystemInfo mSystemInfo){
        logger.info("获取复旦大学系统的信息 : "+ mSystemInfo);
        executorService.submit(() -> {
            // 获取了所有的服务信息
            Map<String, MPathInfo> map = GetSystemCodeAndPath.getcode(mSystemInfo.getGitUrl(), mSystemInfo.getBranch());
            logger.info("获取的服务信息为 : " + map);
            List<MService> allMService = new ArrayList<>();
            for (Map.Entry<String, MPathInfo> entry : map.entrySet()){
                MService mService = GetServiceInfo.getMservice(entry.getValue());
                allMService.add(mService);
            }
//            List<MService> allMService =
//                    map.values().stream().map(mPathInfo-> GetServiceInfo.getMservice(mPathInfo)).collect(Collectors.toList());
            serverClient.pushServiceInfos(allMService, mSystemInfo.getBranch());
        });
        return MResponse.successResponse().code(1);
    }
}