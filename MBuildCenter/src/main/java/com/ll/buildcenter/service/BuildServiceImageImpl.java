package com.ll.buildcenter.service;

import com.ll.common.config.ServiceConfig;
import com.ll.common.service.MSvcVersion;
import com.ll.common.utils.MShellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/7
 */
@Service("BuildServiceImageImpl")
public class BuildServiceImageImpl implements BuildServiceImage{



    @Value("${docker.registry.ip}")
    private String registryIp;
    @Value("${docker.registry.port}")
    private int port;


    private static String allname = "train-ticket";

    @Override
    public String buildServiceImage(String branch, String serviceName, String serviceVersion) {
        String serviceImage = serviceName.toLowerCase() + ":" + MSvcVersion.fromStr(serviceVersion).toCommonStr();
        String localPath = ServiceConfig.CODE_DIWNLOAD_PATH + File.separator + allname+"_"+branch + File.separator + serviceName.toLowerCase();
        String strSh = new StringBuilder("docker build -t ").append(serviceImage).append(" .").toString();
        return MShellUtils.runShellWithOne(strSh, new File(localPath));
    }

    @Override
    public boolean compile(String branch) {
        String localPath = ServiceConfig.CODE_DIWNLOAD_PATH + File.separator + allname+"_" + branch + "/";
        String sh = "mvn clean package -Dmaven.test.skip=true";
        return MShellUtils.runShellWithOne(sh, new File(localPath))==null?false:true;
    }

    // push
    @Override
    public boolean pushImage(String imagesName) {
        // docker tag
        String dockerRe = new StringBuilder().append(registryIp).append(":").append(port).append("/").append(imagesName).toString();;
        String shStr = new StringBuilder("docker tag ")
                .append(imagesName)
                .append(" ").append(dockerRe).toString();
        //docker push
        String shStr1 =  new StringBuilder("docker push ").append(dockerRe).toString();
        MShellUtils.onlyRunShell(shStr);
        return MShellUtils.runShellWithOne(shStr1)==null?false:true;
    }
}
