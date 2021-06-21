package com.ll.centralcontrol.service;

import com.ll.common.service.MService;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */
public interface ServiceAnalysis {


    void pushInfo(List<MService> list, String branch);
}
