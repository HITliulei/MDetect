package com.ll.centralcontrol.service.Impl;

import com.ll.centralcontrol.service.DeploymentService;
import com.ll.centralcontrol.utils.MDatabaseUtils;
import com.ll.common.bean.EX.DeServiceInfo;
import com.ll.common.bean.EX.ServiceInfo;
import com.ll.common.bean.system.MServiceInfo;
import com.ll.common.service.MService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */
@Service("deploymentService")
public class DeploymentServiceImpl implements DeploymentService {

    @Autowired
    private MDatabaseUtils mDatabaseUtils;

    @Override
    public DeServiceInfo getServiceInfoList(String branch) {
        List<MService> mServices =  mDatabaseUtils.getAllServices();
        DeServiceInfo deServiceInfo = new DeServiceInfo();
        List<ServiceInfo> mServiceInfos = new ArrayList<>();
        for (MService mService : mServices){
            if (branch.equals(mService)){
                mServiceInfos.add(new ServiceInfo(mService.getServiceName(), mService.getServiceVersion().toCommonStr(), mService.getPort()));
            }
        }
        deServiceInfo.setServiceInfos(mServiceInfos);
        return deServiceInfo;
    }
}
