package com.ll.centralcontrol.controller;

import com.ll.centralcontrol.client.DeploymentClient;
import com.ll.centralcontrol.service.DeploymentService;
import com.ll.centralcontrol.service.Impl.DeploymentServiceImpl;
import com.ll.common.bean.EX.DeServiceInfo;
import com.ll.common.utils.MResponse;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */

@RestController
@RequestMapping("/api/v1/deployment")
public class DeploymentController {

    @Autowired
    private DeploymentClient deploymentClient;

    @Resource(name = "deploymentService", type = DeploymentServiceImpl.class)
    private DeploymentService deploymentService;

    /**
     * 部署某个分支的结构
     * @param name 实验的名称
     * @param times 实验的次数
     * @param branch 实验室用的分支
     * @return
     */
    @GetMapping("/delpoyment")
    public MResponse deployment(@RequestParam("name") String name, @RequestParam("times") String times, @RequestParam("branch")String branch){
        // 根据服务名称获取 相应的版本和分支，这个具体怎么操作还是看后续的研究。
        DeServiceInfo deServiceInfo = deploymentService.getServiceInfoList(branch);
        deServiceInfo.setExName(name);
        deServiceInfo.setExTimes(times);
        deploymentClient.deployment(name, times, deServiceInfo);
        return MResponse.successResponse();
    }


    /**
     * 同样的部署结构
     * @param name 实验的名称
     * @param times  实验的次数
     * @param branch 实验使用的分支
     * @return
     */
    @GetMapping("/deploymentexit")
    public MResponse deployexit(@RequestParam("name") String name, @RequestParam("times") String times, @RequestParam("branch")String branch){



        return MResponse.successResponse();
    }


    @GetMapping("/deploymentCommand")
    public MResponse deploycommand(@RequestHeader HttpHeaders httpHeaders){
        deploymentClient.deploymentCommand(httpHeaders);

        return MResponse.successResponse().code(1);
    }
}
