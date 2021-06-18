package com.ll.centralcontrol.controller;

import com.ll.centralcontrol.client.DeploymentClient;
import com.ll.common.utils.MResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */

@RestController
@RequestMapping("/deployment")
public class DeploymentController {


    @Autowired
    private DeploymentClient deploymentClient;

    @GetMapping("/delpoyment")
    public MResponse deployment(@RequestParam("name") String name, @RequestParam("times") String times){
        deploymentClient.deployment(name, times);
        return MResponse.successResponse();
    }
}
