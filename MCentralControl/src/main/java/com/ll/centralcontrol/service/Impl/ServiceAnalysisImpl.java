package com.ll.centralcontrol.service.Impl;

import com.ll.centralcontrol.client.BuilderCenterClient;
import com.ll.centralcontrol.service.ServiceAnalysis;
import com.ll.centralcontrol.utils.MDatabaseUtils;
import com.ll.common.service.MService;
import com.ll.common.service.MSvcInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */
@Component
@Qualifier("serviceAnalysisImpl")
public class ServiceAnalysisImpl implements ServiceAnalysis {

    private static Logger logger = LogManager.getLogger(ServiceAnalysisImpl.class);

    private static Set<String> allServiceId  = new HashSet<String>();

    @Autowired
    private BuilderCenterClient builderCenterClient;


    @Autowired
    private MDatabaseUtils mDatabaseUtils;

    @Override
    public void pushInfo(List<MService> serviceList, String branch) {
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
    }
}
