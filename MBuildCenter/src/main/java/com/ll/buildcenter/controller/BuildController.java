package com.ll.buildcenter.controller;

import com.ll.buildcenter.api.JobApi;
import com.ll.buildcenter.service.BuildServiceImage;
import com.ll.common.bean.MBuildInfo;
import com.ll.common.bean.MBuildJobBean;
import com.ll.common.bean.MBuildJobFinishedBean;
import com.ll.common.service.MSvcVersion;
import com.ll.common.utils.MResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/6
 */

@RestController
@RequestMapping("buildCenter")
public class BuildController {


    private static Logger logger = LogManager.getLogger(BuildController.class);

    @GetMapping("hello")
    public String welcome(){
        return "hello mbuildcenter";
    }


    @Autowired
    @Qualifier("BuildServiceImageImpl")
    private BuildServiceImage buildServiceImage;
    private static ExecutorService executorService = Executors.newFixedThreadPool(8);


    @RequestMapping("/build")
    public MResponse buildJob(@RequestBody MBuildJobBean mBuildJobBean){
        String jobName = mBuildJobBean.getServiceName()+"_"+mBuildJobBean.getServiceVersion();
        JobApi.createJob(mBuildJobBean,jobName);
        String result = JobApi.buildJob(jobName);
        if(result.equals("SUCCESS")){
            MBuildJobFinishedBean mBuildJobFinishedBean = new MBuildJobFinishedBean();
            mBuildJobFinishedBean.setId(jobName);
            mBuildJobFinishedBean.setImageUrl("micheallei/"+mBuildJobBean.getServiceName().toLowerCase()+":"+mBuildJobBean.getServiceVersion());
            mBuildJobFinishedBean.setSuccess(true);
            return MResponse.successResponse().data(mBuildJobBean).code(1);
        }else{
            return MResponse.failedResponse().data(null).code(1);
        }
    }


    @PostMapping("buildImage")
    public MResponse buildImage(@RequestBody MBuildInfo mBuildInfo){
        // ??????????????????????????????????????? ?????????docker??????????????????????????????
        logger.info("??????????????????");
        String string = buildServiceImage.buildServiceImage(mBuildInfo.getBranch(),mBuildInfo.getServiceName(), mBuildInfo.getServiceVersion());
        String serviceImageName = mBuildInfo.getServiceName().toLowerCase() + ":" + MSvcVersion.fromStr(mBuildInfo.getServiceVersion()).toCommonStr();
//        boolean ifpush = buildServiceImage.pushImage(serviceImageName);
        return string==null?MResponse.failedResponse().code(0).status("failed"):MResponse.successResponse().code(1).data(string+ " " + " push success");
    }

    @GetMapping("compile/{branch}")
    public MResponse complieProject(@PathVariable("branch") String branch){
        logger.info("??????????????????????????????");
        String string = buildServiceImage.compile(branch)?"build success":"build failed";
        logger.info("????????????");
        return new MResponse().status(string);
    }
}
