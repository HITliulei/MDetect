package com.ll.buildcenter.controller;

import com.ll.buildcenter.api.JobApi;
import com.ll.buildcenter.service.BuildServiceImage;
import com.ll.common.bean.MBuildInfo;
import com.ll.common.bean.MBuildJobBean;
import com.ll.common.bean.MBuildJobFinishedBean;
import com.ll.common.service.MSvcVersion;
import com.ll.common.utils.MResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/6
 */


@RequestMapping("buildCenter")
public class BuildController {


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
        // 这次通过脚本自动构建镜像， 并推送docker镜像到私有的镜像仓库
        String string = buildServiceImage.buildServiceImage(mBuildInfo.getServiceName(), mBuildInfo.getServiceVersion());
        String serviceImageName = mBuildInfo.getServiceName().toLowerCase() + ":" + MSvcVersion.fromStr(mBuildInfo.getServiceVersion()).toCommonStr();
        boolean ifpush = buildServiceImage.pushImage(serviceImageName);
        return ifpush?MResponse.failedResponse().code(0).status("failed"):MResponse.successResponse().code(1).data(string+ " " + " push success");
    }

    @GetMapping("compile")
    public MResponse complieProject(){
        return new MResponse().status(buildServiceImage.compile()?"build success":"build failed");
    }
}
