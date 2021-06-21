package com.ll.centralcontrol.service;

import com.ll.common.bean.EX.DeServiceInfo;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */
public interface DeploymentService {

    DeServiceInfo getServiceInfoList(String branch);
}
